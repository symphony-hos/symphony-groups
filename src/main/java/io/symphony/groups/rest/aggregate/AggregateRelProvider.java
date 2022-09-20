package io.symphony.groups.rest.aggregate;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.stereotype.Component;

import io.symphony.groups.rest.aggregate.model.AggregateModel;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AggregateRelProvider implements LinkRelationProvider {
	
	@Override
	public LinkRelation getCollectionResourceRelFor(final Class<?> type) {
		return LinkRelation.of("aggregates");
	}

	@Override
	public LinkRelation getItemResourceRelFor(Class<?> type) {
		return LinkRelation.of("aggregate");
	}

	@Override
	public boolean supports(LookupContext delimiter) {
		return AggregateModel.class.isAssignableFrom(delimiter.getType());
	}

}
