package io.symphony.groups.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.symphony.groups.rest.aggregate.model.AggregateModel;
import io.symphony.groups.rest.group.GroupModel;
import lombok.RequiredArgsConstructor;

@Controller
@ResponseBody
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RootController {
	
	private final EntityLinks links;
	
	@GetMapping
	public ResponseEntity<EntityModel<Map<String, Object>>> getRoot() {
		EntityModel<Map<String, Object>> em = EntityModel.of(Map.of());
		em.add(links.linkToCollectionResource(GroupModel.class).withRel("groups"));
		em.add(links.linkToCollectionResource(AggregateModel.class).withRel("aggregates"));
		return new ResponseEntity<EntityModel<Map<String, Object>>>(em, HttpStatus.OK);
	}

}