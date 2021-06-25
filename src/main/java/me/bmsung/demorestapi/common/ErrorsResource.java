package me.bmsung.demorestapi.common;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.bmsung.demorestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {

    @JsonUnwrapped
    private Errors errors;

    public ErrorsResource(Errors errors){
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
        this.errors = errors;
    }
}