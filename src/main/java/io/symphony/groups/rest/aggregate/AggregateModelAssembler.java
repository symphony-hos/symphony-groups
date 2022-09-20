package io.symphony.groups.rest.aggregate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import io.symphony.groups.data.aggregate.Aggregate;
import io.symphony.groups.data.aggregate.AlarmAggregate;
import io.symphony.groups.data.aggregate.ContactAggregate;
import io.symphony.groups.data.aggregate.QuantityAggregate;
import io.symphony.groups.data.aggregate.SwitchAggregate;
import io.symphony.groups.rest.aggregate.model.AggregateModel;
import io.symphony.groups.rest.aggregate.model.AlarmAggregateModel;
import io.symphony.groups.rest.aggregate.model.ContactAggregateModel;
import io.symphony.groups.rest.aggregate.model.QuantityAggregateModel;
import io.symphony.groups.rest.aggregate.model.SwitchAggregateModel;

@Component
public class AggregateModelAssembler implements RepresentationModelAssembler<Aggregate, AggregateModel<?, ?>> {

	private EntityLinks links;

	public AggregateModelAssembler(@Autowired EntityLinks links) {
		this.links = links;
	}

	@Override
	public AggregateModel<?, ?> toModel(Aggregate entity) {
		AggregateModel<?, ?> model = null;
		if (entity instanceof QuantityAggregate)
			model = new QuantityAggregateModel((QuantityAggregate) entity);
		else if (entity instanceof SwitchAggregate)
			model = new SwitchAggregateModel((SwitchAggregate) entity);
		else if (entity instanceof ContactAggregate)
			model = new ContactAggregateModel((ContactAggregate) entity);
		else if (entity instanceof AlarmAggregate)
			model = new AlarmAggregateModel((AlarmAggregate) entity);
		
		if (model == null)	
			return null;
		
		model.add(links.linkToItemResource(AggregateModel.class, entity.getId()));
		model.add(linkTo(methodOn(AggregateController.class).points(entity.getId())).withRel("points"));
		
		return model;
	}


}
