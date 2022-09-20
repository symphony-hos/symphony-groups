package io.symphony.groups.rest.aggregate.model;

import io.symphony.common.point.data.state.type.Alarm;
import io.symphony.groups.data.aggregate.AlarmAggregate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmAggregateModel extends StateAggregateModel<Alarm, AlarmAggregate> {

	private final String type = "AlarmAggregate";
	
	public AlarmAggregateModel() {
		this(null);
	}
	
	public AlarmAggregateModel(AlarmAggregate aggr) {
		super(AlarmAggregate.class, aggr);
	}
	
}
