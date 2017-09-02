package io.github.cepr0.dependent_security.security;

import io.github.cepr0.dependent_security.model.Category;
import io.github.cepr0.dependent_security.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * @author Cepro, 2017-09-02
 */
@RequiredArgsConstructor
@Component
public class UserService implements UserDetailsService {

	private final UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		return userRepo.findByName(name)
				.map(user -> new AuthUser(
						user.getName(),
						user.getPassword(),
						user.getRoles(),
						user.getCategories().stream().map(Category::getId).collect(toList())))
				.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
	}

	public boolean checkCategory(Authentication auth, Long id) {
		return auth != null &&
				auth.getPrincipal() instanceof AuthUser &&
				getAccessedCategoryIds(auth.getPrincipal()).contains(id);
	}

	public List<Long> getAccessedCategoryIds(Object principal) {
		return (principal instanceof AuthUser) ?
				((AuthUser) principal).getAccessedCategoryIds() :
				emptyList();
	}
}
