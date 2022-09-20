package io.symphony.groups.rest.aggregate.model;

import io.symphony.common.point.data.state.type.Switch;
import io.symphony.groups.data.aggregate.SwitchAggregate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SwitchAggregateModel extends StateAggregateModel<Switch, SwitchAggregate> {

	private final String type = "SwitchAggregate";
	
	public SwitchAggregateModel() {
		this(null);
	}
	
	public SwitchAggregateModel(SwitchAggregate aggr) {
		super(SwitchAggregate.class, aggr);
	}
	
}
