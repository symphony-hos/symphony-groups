package io.symphony.groups.properties;

import java.util.Map;

import io.symphony.common.point.data.state.type.Alarm;
import io.symphony.common.point.data.state.type.Contact;
import io.symphony.common.point.data.state.type.Switch;
import io.symphony.common.selector.SelectorPropertiesLoader;
import io.symphony.groups.data.aggregate.Aggregate;
import io.symphony.groups.data.aggregate.AlarmAggregate;
import io.symphony.groups.data.aggregate.ContactAggregate;
import io.symphony.groups.data.aggregate.QuantityAggregate;
import io.symphony.groups.data.aggregate.StateAggregate;
import io.symphony.groups.data.aggregate.SwitchAggregate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AggregatePropertiesLoader {
	
	public static enum Type { Quantity, Switch, Contact, Alarm };

	/* COMMON */
	
	private String id;
	
	private Map<String, String> labels;
	
	private SelectorPropertiesLoader selector;
	
	private Type type;
	
	
	/* QUANTITY */
	
	private QuantityAggregate.Mode operator;
	
	private Integer precision = 2;

	
	/* STATE */
	
	private StateAggregate.Aggregator logic;

	private String match;
	
	private String defaultState;
	
	
	public Aggregate load() {
		Aggregate aggr = null;
		if (type == Type.Quantity)
			aggr = loadQuantity();
		else
			aggr = loadState();
		aggr.setId(id);
		aggr.setLabels(labels);
		aggr.setSelector(selector.load());
		return aggr;
	}
	
	private QuantityAggregate loadQuantity() {
		return QuantityAggregate.builder()
			.mode(operator)
			.precision(precision)
			.build();
	}
	
	private StateAggregate<?> loadState() {
		StateAggregate<?> aggr = null;
		
		if (type == Type.Switch)
			aggr = SwitchAggregate.builder()
				.match(Switch.valueOf(match))	
				.defaultState(Switch.valueOf(defaultState))
				.build();	
		else if (type == Type.Contact)
			aggr = ContactAggregate.builder()
				.match(Contact.valueOf(match))	
				.defaultState(Contact.valueOf(defaultState))	
				.build();
		else if (type == Type.Alarm)
			aggr = AlarmAggregate.builder()
				.match(Alarm.valueOf(match))
				.defaultState(Alarm.valueOf(defaultState))
				.build();
		
		if (aggr == null)
			throw new RuntimeException("Could not create StateAggregate from " + this);
		
		aggr.setMode(logic);
		return aggr;
	}
	
}
