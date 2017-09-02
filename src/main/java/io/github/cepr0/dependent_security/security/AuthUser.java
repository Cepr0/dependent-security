package io.github.cepr0.dependent_security.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * @author Cepro, 2017-09-02
 */
class AuthUser extends User {

	@Getter
	private List<Long> accessedCategoryIds;

	AuthUser(String username,
	         String password,
	         Collection<? extends GrantedAuthority> authorities,
	         List<Long> accessedCategoryIds
	) {
		super(username, password, authorities);
		this.accessedCategoryIds = accessedCategoryIds;
	}
}
