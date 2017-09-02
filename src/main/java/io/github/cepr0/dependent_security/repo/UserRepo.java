package io.github.cepr0.dependent_security.repo;

import io.github.cepr0.dependent_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * @author Cepro, 2017-08-31
 */
@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
@RepositoryRestResource(exported = false)
public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByName(String name);

	@Query("select c.id from User u join u.categories c where u.name = ?1")
	List<Long> getCategoryIdsByName(String name);

	@Query("select c.id from User u join u.categories c join c.rooms r where u.name = ?1 and r.id = ?2")
	List<Long> getCategoryIdsByNameAndRoomId(String name, Long id);

}
