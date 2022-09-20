package io.symphony.groups.data.aggregate;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.state.AlarmPoint;
import io.symphony.common.point.data.state.type.Alarm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Document(collection = "aggregate")
public class AlarmAggregate extends StateAggregate<Alarm> {
	
	@Transient
	private final String type = "AlarmAggregate";
	
	public AlarmAggregate() {
		super(Alarm.class);
	}
	
	@Override
	public Point asPoint() {
		return AlarmPoint.builder()
			.access(getAccess())
			.id(getId())
			.labels(getLabels())
			.state(getState())
			.build();
	}

}
