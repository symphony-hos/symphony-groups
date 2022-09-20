package io.symphony.groups.data.aggregate;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.state.SwitchPoint;
import io.symphony.common.point.data.state.type.Switch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Document(collection = "aggregate")
public class SwitchAggregate extends StateAggregate<Switch> {
	
	@Transient
	private final String type = "SwitchAggregate";

	public SwitchAggregate() {
		super(Switch.class);
	}
	
	@Override
	public Point asPoint() {
		return SwitchPoint.builder()
			.access(getAccess())
			.id(getId())
			.labels(getLabels())
			.state(getState())
			.build();
	}
	
}
