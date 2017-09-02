package io.github.cepr0.dependent_security.repo;

import io.github.cepr0.dependent_security.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Cepro, 2017-08-31
 */
@RepositoryRestResource
public interface CategoryRepo extends JpaRepository<Category, Long> {

	@Override
	@Query("select c from Category c where c.id in (" + "?#{@userRepo.getCategoryIdsByName(principal.username)}" + ")")
	Page<Category> findAll(Pageable pageable);
}
