package io.symphony.groups.rest.aggregate.model;

import io.symphony.common.point.data.state.type.Contact;
import io.symphony.groups.data.aggregate.ContactAggregate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ContactAggregateModel extends StateAggregateModel<Contact, ContactAggregate> {

	private final String type = "ContactAggregate";
	
	public ContactAggregateModel() {
		this(null);
	}
	
	public ContactAggregateModel(ContactAggregate aggr) {
		super(ContactAggregate.class, aggr);
	}
	
}
