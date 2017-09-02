package io.github.cepr0.dependent_security.security;

import io.github.cepr0.dependent_security.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.springframework.http.HttpMethod.*;

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

		// https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#el-access-web-beans

		http.httpBasic().realmName("dependent-security").and().csrf().disable().authorizeRequests()
				.antMatchers(GET, ROOT_BASE_AND_PROFILE).permitAll()
				.antMatchers(GET, CATEGORIES + "/{id}/*")
					.access("@userService.checkCategory(authentication, #id)")
				.antMatchers(GET, CATEGORIES + "/{id}/rooms/*")
					.access("@userService.checkCategory(authentication, #id)")
				.antMatchers(PATCH, CATEGORIES + "/{id}/rooms/*")
					.access("@userService.checkCategory(authentication, #id)")
				.antMatchers(PUT, CATEGORIES + "/{id}/rooms/*/booking")
					.access("@userService.checkCategory(authentication, #id)")
				.antMatchers(GET, CATEGORIES + "/**").authenticated()
				.anyRequest().hasRole("ADMIN");
	}
}
