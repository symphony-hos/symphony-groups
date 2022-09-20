package io.symphony.groups.data.aggregate;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.measure.Unit;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import io.symphony.common.point.data.Point;
import io.symphony.common.point.IQuantityPoint;
import io.symphony.common.point.data.quantity.QuantityPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Document(collection = "aggregate")
public class QuantityAggregate extends Aggregate implements IQuantityPoint {

	public static enum Mode {
		MIN((values) -> {
			DoubleSummaryStatistics stats = values.stream().mapToDouble(v -> v).summaryStatistics();
			return Result.builder().value(stats.getMin()).build();
		}), 
		MAX((values) -> {
			DoubleSummaryStatistics stats = values.stream().mapToDouble(v -> v).summaryStatistics();
			return Result.builder().value(stats.getMax()).build();
		}), 
		MEAN((values) -> {
			DoubleSummaryStatistics stats = values.stream().mapToDouble(v -> v).summaryStatistics();
			return Result.builder().value(stats.getAverage()).build();
		}),
		SUM((values) -> {
			DoubleSummaryStatistics stats = values.stream().mapToDouble(v -> v).summaryStatistics();
			return Result.builder().value(stats.getSum()).build();
		});
		
		@Data
		@Builder
		public static class Result {
			private Double value;
			private Double deviation;
		}
		
		private Function<List<Double>, Result> func;
		
		private Mode(Function<List<Double>, Result> func) {
			this.func = func;
		}
		
		public Result calculate(List<Double> values) {
			return func.apply(values);
		}
		
	}
	
	private Mode mode;

	private Unit<?> unit;

	private Double value;
	
	@Default
	private Integer precision = 2;

	@Transient
	private final String type = "QuantityAggregate";

	@Override
	public void calculate() {
		super.calculate();
		
		List<Unit<?>> units = getPoints().stream()
				.filter(i -> i != null && i.getContent() != null && i.getContent() instanceof QuantityPoint)
				.map(i -> (QuantityPoint) i.getContent())
				.map(q -> q.getUnit())
				.distinct()
				.collect(Collectors.toList());
		
		Unit<?> unit = units.size() == 1 ? units.get(0) : null;

		if (unit == null) {
			setUnit(null);
			setValue(null);
			return;
		}
		
		List<Double> values = getPoints().stream()
			.filter(i -> i != null && i.getContent() != null && i.getContent() instanceof QuantityPoint)
			.map(i -> (QuantityPoint) i.getContent())
			.map(p -> p.getValue())
			.collect(Collectors.toList());
		
		Mode mode = getMode();
		Double value = mode.calculate(values).getValue();
		if (getPrecision() != null) {
			double factor = Math.pow(10, getPrecision());
			value = Math.round(value * Math.pow(10, getPrecision())) / factor;
		}
		
		setValue(value);
		setUnit(unit);
	}

	@Override
	public Point asPoint() {
		return QuantityPoint.builder()
			.access(getAccess())
			.id(getId())
			.labels(getLabels())
			.unit(getUnit())
			.value(getValue())
			.build();
	}

}
