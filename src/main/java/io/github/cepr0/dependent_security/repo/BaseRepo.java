package io.github.cepr0.dependent_security.repo;

import io.github.cepr0.dependent_security.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Cepro, 2017-08-31
 */
@RepositoryRestResource(exported = false)
public interface BaseRepo extends JpaRepository<BaseEntity, Long> {
}
