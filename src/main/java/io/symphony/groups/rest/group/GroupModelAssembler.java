package io.symphony.groups.rest.group;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import io.symphony.groups.data.group.Group;

@Component
public class GroupModelAssembler extends RepresentationModelAssemblerSupport<Group, GroupModel> {

	private final EntityLinks links;

	public GroupModelAssembler(@Autowired EntityLinks links) {
		super(GroupController.class, GroupModel.class);
		this.links = links;
	}

	@Override
	public GroupModel toModel(Group entity) {
		GroupModel model = this.instantiateModel(entity);
		model.setLabels(entity.getLabels());
		model.setSelector(entity.getSelector());
		model.add(links.linkToItemResource(GroupModel.class, entity.getId()).withSelfRel());
		model.add(linkTo(methodOn(GroupController.class).points(entity.getId())).withRel("points"));
		return model;
	}

}
