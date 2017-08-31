package io.github.cepr0.dependent_security;

import io.github.cepr0.dependent_security.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		String BASE = configuration.getBasePath().getPath();

		String[] ROOT_BASE_AND_PROFILE = {"/*", BASE, BASE + "/", BASE + "/profile/**"};
		String[] USER_PROFILE = {BASE + "/userProfile", BASE + "/userProfile/"};
		String[] COMMON_ELEMENTS = {BASE + "/polls/**", BASE + "/menus/**", BASE + "/restaurants/**", BASE + "/userProfile/**"};
		String VOTE = BASE + "/menus/*/vote";
		String HAL_BROWSER = BASE + "/browser/**";

		http.httpBasic().realmName("dependent-security").and().csrf().disable().authorizeRequests()
				.antMatchers(HAL_BROWSER).authenticated()
				.antMatchers(GET, ROOT_BASE_AND_PROFILE).permitAll()
				.antMatchers(POST, USER_PROFILE).permitAll()
				.antMatchers(GET, COMMON_ELEMENTS).authenticated()
				.antMatchers(PUT, VOTE).authenticated()
				.anyRequest().hasRole("ADMIN");
	}

	@RequiredArgsConstructor
	@Component
	public static class UserService implements UserDetailsService {

		private final UserRepo repo;

		@Override
		public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
			return repo.findByName(name).map(user -> new User(user.getName(), user.getPassword(),
					createAuthorityList(user.getRole().getAuthority())))
					.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
		}
	}
}
