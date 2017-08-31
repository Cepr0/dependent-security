package io.github.cepr0.dependent_security.repo;

import io.github.cepr0.dependent_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * @author Cepro, 2017-08-31
 */
@RepositoryRestResource(exported = false)
public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByName(String name);
}
