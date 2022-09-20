package io.symphony.groups.rest.aggregate.model;

import io.symphony.common.point.data.Access;
import io.symphony.common.point.data.state.type.State;
import io.symphony.groups.data.aggregate.StateAggregate;
import io.symphony.groups.data.aggregate.StateAggregate.Aggregator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class StateAggregateModel<T extends State, A extends StateAggregate<T>>
		extends AggregateModel<StateAggregateModel<T, ?>, A> {

	private Aggregator mode;

	private T match;

	private T state;

	public StateAggregateModel(Class<A> entityType, A aggr) {
		super(entityType, aggr);
		if (aggr != null) {
			this.mode = aggr.getMode();
			this.match = aggr.getMatch();
			this.state = aggr.getState();
		}
	}

	@Override
	public void update(A aggr, boolean onlyNonNull) {
		super.update(aggr, onlyNonNull);

		// Mode
		if (!onlyNonNull || (onlyNonNull && mode != null)) {
			aggr.setMode(mode);
		}

		// Match
		if (!onlyNonNull || (onlyNonNull && match != null)) {
			aggr.setMatch(match);
		}

		// State
		if (!onlyNonNull || (onlyNonNull && state != null) && aggr.getAccess().contains(Access.WRITE)) {
			aggr.setState(state);
		}

	}

}
