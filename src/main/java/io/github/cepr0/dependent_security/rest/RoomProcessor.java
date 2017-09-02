package io.github.cepr0.dependent_security.rest;

import io.github.cepr0.dependent_security.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * @author Cepro, 2017-09-02
 */
@RequiredArgsConstructor
@Component
public class RoomProcessor implements ResourceProcessor<Resource<Room>> {

	private final EntityLinks entityLinks;

	@Override
	public Resource<Room> process(Resource<Room> resource) {

		Room room = resource.getContent();
		Long categoryId = room.getCategory().getId();

		Link roomLink = new Link(entityLinks
				.linkForSingleResource(room)
				.withSelfRel()
				.getHref()
				.replaceFirst("/rooms", format("/categories/%d/rooms", categoryId)));

		resource.add(
				roomLink,
				new Link(roomLink.getHref() + "/booking", "booking"));

		return resource;
	}
}
