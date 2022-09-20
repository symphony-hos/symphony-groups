package io.symphony.groups.rest.group;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.symphony.common.point.data.PointSnapshot;
import io.symphony.groups.data.group.Group;
import io.symphony.groups.data.group.GroupRepo;
import lombok.RequiredArgsConstructor;

@ExposesResourceFor(GroupModel.class)
@Controller
@ResponseBody
@RequestMapping("/groups")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupController {

	private final GroupRepo groupRepo;

	private final GroupModelAssembler assembler;

	@GetMapping
	public ResponseEntity<CollectionModel<GroupModel>> all() {
		Iterable<Group> groups = groupRepo.findAll();

		Set<GroupModel> models = StreamSupport.stream(groups.spliterator(), false).map(g -> assembler.toModel(g))
				.collect(Collectors.toSet());

		CollectionModel<GroupModel> collection = CollectionModel.of(models);

		return ResponseEntity.ok(collection);
	}

	@GetMapping("{id}")
	public ResponseEntity<GroupModel> one(@PathVariable String id) {
		Optional<Group> group = groupRepo.findById(id);

		if (group.isEmpty())
			return null;

		return ResponseEntity.ok(assembler.toModel(group.get()));
	}
	
	@GetMapping("{id}/points")
	public ResponseEntity<CollectionModel<PointSnapshot>> points(@PathVariable String id) {
		Optional<Group> group = groupRepo.findById(id);

		if (group.isEmpty())
			return null;

		return ResponseEntity.ok(CollectionModel.of(group.get().getPoints()));
	}

	@PostMapping
	public ResponseEntity<GroupModel> create(@RequestBody GroupModel model) {
		Group group = Group.builder().id(model.getId()).labels(model.getLabels())
				.selector(model.getSelector()).build();

		Optional<Group> existing = groupRepo.findById(model.getId());
		if (existing.isPresent())
			throw new RuntimeException("Point with id " + model.getId() + " already exists.");

		group = groupRepo.save(group);
		return ResponseEntity.ok(assembler.toModel(group));
	}

	@PatchMapping("{id}")
	public ResponseEntity<GroupModel> create(@PathVariable String id, @RequestBody GroupModel model) {
		Optional<Group> existing = groupRepo.findById(model.getId());
		if (existing.isEmpty())
			throw new RuntimeException("Point with id " + model.getId() + " does not exist.");

		Group group = existing.get();

		if (model.getLabels() != null)
			for (String label : model.getLabels().keySet())
				group.getLabels().put(label, model.getLabels().get(label));

		if (model.getSelector() != null)
			group.setSelector(model.getSelector());

		group = groupRepo.save(group);
		return ResponseEntity.ok(assembler.toModel(group));
	}

}
