package io.github.cepr0.dependent_security.repo;

import io.github.cepr0.dependent_security.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Cepro, 2017-08-30
 */
@RepositoryRestResource
public interface RoomRepo extends JpaRepository<Room, Long> {
}
