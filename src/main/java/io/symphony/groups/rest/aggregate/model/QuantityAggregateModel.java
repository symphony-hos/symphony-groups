package io.symphony.groups.rest.aggregate.model;

import javax.measure.Unit;

import io.symphony.common.point.data.Access;
import io.symphony.groups.data.aggregate.QuantityAggregate;
import io.symphony.groups.data.aggregate.QuantityAggregate.Mode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QuantityAggregateModel extends AggregateModel<QuantityAggregateModel, QuantityAggregate> {

	private Mode mode;

	private Unit<?> unit;

	private Double value;

	private Integer precision;

	private final String type = "QuantityAggregate";
	
	public QuantityAggregateModel() {
		this(null);
	}
	
	public QuantityAggregateModel(QuantityAggregate aggr) {
		super(QuantityAggregate.class, aggr);
		if (aggr != null) {
			this.mode = aggr.getMode();
			this.unit = aggr.getUnit();
			this.value = aggr.getValue();
			this.precision = aggr.getPrecision();
		}
	}

	@Override
	public void update(QuantityAggregate aggr, boolean onlyNonNull) {
		super.update(aggr, onlyNonNull);

		// Mode
		if (!onlyNonNull || (onlyNonNull && mode != null)) {
			aggr.setMode(mode);
		}

		// Value
		if (!onlyNonNull || (onlyNonNull && value != null) && aggr.getAccess().contains(Access.WRITE)) {
			aggr.setValue(value);
		}

		// Precision
		if (!onlyNonNull || (onlyNonNull && precision != null)) {
			aggr.setPrecision(precision);
		}
	}

}
