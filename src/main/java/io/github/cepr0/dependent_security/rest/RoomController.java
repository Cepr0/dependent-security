package io.github.cepr0.dependent_security.rest;

import io.github.cepr0.dependent_security.model.Room;
import io.github.cepr0.dependent_security.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cepro, 2017-09-02
 */
@RequiredArgsConstructor
@RepositoryRestController
@RequestMapping("/categories/{id}/rooms/{roomId}")
public class RoomController {

	private final RoomRepo roomRepo;
	private final EntityLinks entityLinks;

	@GetMapping
	public ResponseEntity<?> get(@PathVariable("id") Long categoryId, @PathVariable("roomId") Room room) {
		return room.getCategory().getId().compareTo(categoryId) == 0 ?
				ResponseEntity.ok(toResource(room)) :
				ResponseEntity.notFound().build();
	}

	/**
	 * An example of editing the {@link Room}
	 */
	@PatchMapping
	public ResponseEntity<?> patch(@PathVariable("id") Long categoryId, @PathVariable("roomId") Room target, @RequestBody Room source) {
		if (target.getCategory().getId().compareTo(categoryId) == 0) {
			target.setDescription(source.getDescription());
			roomRepo.save(target);
			return ResponseEntity.ok(toResource(target));
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * An example of the booking request
	 */
	@PutMapping("/booking")
	public ResponseEntity<?> book(@PathVariable("id") Long categoryId, @PathVariable("roomId") Room room) {
		if (room.getCategory().getId().compareTo(categoryId) == 0) {
			return ResponseEntity.ok(new Resource<>("The room has been booked."));
		}
		return ResponseEntity.notFound().build();
	}

	private Resource<Room> toResource(Room room) {
		return new Resource<>(room, entityLinks.linkForSingleResource(room.getCategory()).withRel("category"));
	}
}
