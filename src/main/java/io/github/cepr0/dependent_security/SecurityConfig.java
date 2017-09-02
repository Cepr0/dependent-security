package io.github.cepr0.dependent_security;

import io.github.cepr0.dependent_security.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.cepr0.dependent_security.model.User.Role.ROLE_ADMIN;
import static io.github.cepr0.dependent_security.model.User.Role.ROLE_USER;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

/**
 * Security configuration
 *
 * @author Cepro, 2017-01-22
 */
@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userService;
	private final RepositoryRestConfiguration configuration;
	private final UserRepo userRepo;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		String BASE = configuration.getBasePath().getPath();
		String CATEGORIES = BASE + "/categories";
		String[] ROOT_BASE_AND_PROFILE = {"/*", BASE, BASE + "/", BASE + "/profile/**"};

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.httpBasic().realmName("dependent-security").and().csrf().disable().authorizeRequests()
				.antMatchers(GET, ROOT_BASE_AND_PROFILE).permitAll()
				.antMatchers(GET, CATEGORIES + "/{id}/*")
					.access("@securityConfig.checkCategory(authentication, #id)")
				.antMatchers(GET, CATEGORIES + "/{id}/rooms/{roomId}")
					.access("@securityConfig.checkCategoryAndRoom(authentication, #id, #roomId)")
				.antMatchers(PATCH, CATEGORIES + "/{id}/rooms/{roomId}")
					.access("@securityConfig.checkCategoryAndRoom(authentication, #id, #roomId)")
				.antMatchers(PUT, CATEGORIES + "/{id}/rooms/{roomId}/booking")
					.access("@securityConfig.checkCategoryAndRoom(authentication, #id, #roomId)")
				.antMatchers(GET, CATEGORIES + "/**").authenticated()
				.anyRequest().hasRole("ADMIN");
	}

	public boolean checkCategory(Authentication authentication, Long id) {
		List<Long> categoryIds = userRepo.getCategoryIdsByName(authentication.getName());
		return categoryIds.contains(id);
	}

	public boolean checkCategoryAndRoom(Authentication authentication, Long id, Long roomId) {
		List<Long> categoryIds = userRepo.getCategoryIdsByNameAndRoomId(authentication.getName(), roomId);
		return categoryIds.contains(id);
	}

	@RequiredArgsConstructor
	@Component
	public static class UserService implements UserDetailsService {

		private final UserRepo repo;

		@Override
		public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
			return repo.findByName(name).map(user -> new User(user.getName(), user.getPassword(),
					(user.getRole() == ROLE_ADMIN) ?
							createAuthorityList(ROLE_USER.getAuthority(), ROLE_ADMIN.getAuthority()) :
							createAuthorityList(user.getRole().getAuthority())))
					.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
		}
	}

	@Component
	public static class SecurityEvaluationContextExtension extends EvaluationContextExtensionSupport {

		@Override
		public String getExtensionId() {
			return "security";
		}

		@Override
		public SecurityExpressionRoot getRootObject() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			return new SecurityExpressionRoot(authentication) {
			};
		}
	}
}
