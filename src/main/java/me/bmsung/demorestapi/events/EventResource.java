package me.bmsung.demorestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource extends RepresentationModel {
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(event.getId());
        add(linkTo(EventController.class).withRel("query-events"));
        add(selfLinkBuilder.withSelfRel());
        add(selfLinkBuilder.withRel("update-event"));
        add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
