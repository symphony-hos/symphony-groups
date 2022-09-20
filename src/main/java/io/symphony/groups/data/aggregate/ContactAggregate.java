package io.symphony.groups.data.aggregate;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.state.ContactPoint;
import io.symphony.common.point.data.state.type.Contact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Document(collection = "aggregate")
public class ContactAggregate extends StateAggregate<Contact> {
	
	@Transient
	private final String type = "ContactAggregate";
	
	public ContactAggregate() {
		super(Contact.class);
	}
	
	@Override
	public Point asPoint() {
		return ContactPoint.builder()
			.access(getAccess())
			.id(getId())
			.labels(getLabels())
			.state(getState())
			.build();
	}

}
