package io.symphony.groups.data.aggregate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import io.symphony.common.point.IStatePoint;
import io.symphony.common.point.data.state.StatePoint;
import io.symphony.common.point.data.state.type.State;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Document(collection = "aggregate")
public abstract class StateAggregate<T extends State> extends Aggregate implements IStatePoint<T> {

	public static enum Aggregator {
		ANY, ALL;
		
		public <T extends State> T aggregate(T matchState, Collection<T> states, T defaultState) {
			long matches = states.stream().filter(c -> c.equals(matchState)).count();
			if (this == ANY && matches > 0)
				return matchState;
			return defaultState;
		}
		
	}
	
	private Aggregator mode;

	private T match;
	
	private T state;
	
	private T defaultState;

	@Transient
	private Class<T> stateType;
	
	
	@Transient
	private final String type = "StateAggregate";
	
	
	public StateAggregate(Class<T> stateType) {
		this.stateType = stateType;
	}
	
	
	@Override
	public void calculate() {
		super.calculate();
		
		// From all PointInfos stored with the Aggregate
		List<T> states = getPoints().stream()
			// Get the Point associated with it
			.map(i -> i.getContent())
			// And check if it is actually a StatePoint. If so, cast it to StatePoint
			.filter(p -> StatePoint.class.isAssignableFrom(p.getClass()))
			.map(p -> (StatePoint<?>) p)
			// Then extract that Point's state
			.map(p -> p.getState())
			// And check if the points state is of the correct type. If so, cast to that type
			.filter(s -> stateType.isAssignableFrom(s.getClass()))
			.map(s -> stateType.cast(s))
			// Then create a list of all states
			.collect(Collectors.toList());
		
		// Let the Aggregator caculate a resulting state and set it
		T state = getMode().aggregate(getMatch(), states, getDefaultState());
		setState(state);
	}

}
