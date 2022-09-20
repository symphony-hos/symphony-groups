package io.symphony.groups.rest.aggregate;

import java.util.HashSet;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.symphony.common.point.data.PointSnapshot;
import io.symphony.groups.data.aggregate.Aggregate;
import io.symphony.groups.data.aggregate.AggregateRepo;
import io.symphony.groups.rest.AggregateEventHandler;
import io.symphony.groups.rest.aggregate.model.AggregateModel;
import lombok.RequiredArgsConstructor;

@ExposesResourceFor(AggregateModel.class)
@Controller
@ResponseBody
@RequestMapping("/aggregates")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AggregateController {

	private final AggregateRepo aggrRepo;

	private final AggregateModelAssembler assembler;
	
	private final AggregateEventHandler handler;


	@GetMapping
	public ResponseEntity<CollectionModel<AggregateModel<?, ?>>> all() {
		Iterable<Aggregate> groups = aggrRepo.findAll();

		Set<AggregateModel<?, ?>> models = StreamSupport.stream(groups.spliterator(), false)
				.map(g -> assembler.toModel(g)).collect(Collectors.toSet());

		CollectionModel<AggregateModel<?, ?>> collection = CollectionModel.of(models);

		return ResponseEntity.ok(collection);
	}

	@GetMapping("{id}")
	public ResponseEntity<AggregateModel<?, ?>> one(@PathVariable String id) {
		Optional<Aggregate> opt = aggrRepo.findById(id);
		if (opt.isEmpty())
			return null;
		Aggregate aggr = opt.get();
		return ResponseEntity.ok(assembler.toModel(aggr));
	}

	@GetMapping("{id}/points")
	public ResponseEntity<CollectionModel<PointSnapshot>> points(@PathVariable String id) {
		Optional<Aggregate> aggr = aggrRepo.findById(id);

		if (aggr.isEmpty())
			return null;
		
		Aggregate aggregate = aggr.get();
		if (aggregate.getPoints() == null)
			aggregate.setPoints(new HashSet<>());
		
		return ResponseEntity.ok(CollectionModel.of(aggr.get().getPoints()));
	}

	@PatchMapping("{id}")
	public ResponseEntity<AggregateModel<?, ?>> update(@PathVariable String id,
			@RequestBody AggregateModel<?, ?> model) {
		Optional<Aggregate> opt = aggrRepo.findById(id);
		if (opt.isEmpty())
			throw new RuntimeException("Could not find aggregate with id " + id);

		Aggregate aggr = opt.get();

		model.updateIfSupported(aggr, true);

		aggrRepo.save(aggr);
		handler.handleChange(aggr);
		
		return ResponseEntity.ok(assembler.toModel(aggr));
	}

}
