package io.symphony.groups.rest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.stereotype.Component;

import io.symphony.common.point.data.PointSnapshot;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PointInfoRelProvider implements LinkRelationProvider {
	
	@Override
	public LinkRelation getCollectionResourceRelFor(final Class<?> type) {
		return LinkRelation.of("points");
	}

	@Override
	public LinkRelation getItemResourceRelFor(Class<?> type) {
		return LinkRelation.of("point");
	}

	@Override
	public boolean supports(LookupContext delimiter) {
		return PointSnapshot.class.isAssignableFrom(delimiter.getType());
	}

}
