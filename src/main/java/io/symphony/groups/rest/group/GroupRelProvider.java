package io.symphony.groups.rest.group;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GroupRelProvider implements LinkRelationProvider {
	
	@Override
	public LinkRelation getCollectionResourceRelFor(final Class<?> type) {
		return LinkRelation.of("groups");
	}

	@Override
	public LinkRelation getItemResourceRelFor(Class<?> type) {
		return LinkRelation.of("group");
	}

	@Override
	public boolean supports(LookupContext delimiter) {
		return GroupModel.class.isAssignableFrom(delimiter.getType());
	}

}
