package io.symphony.groups.data.selector;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.symphony.common.point.IPoint;
import io.symphony.common.point.data.quantity.QuantityPoint;
import io.symphony.common.selector.CompositeSelector;
import io.symphony.common.selector.LabelSelector;
import io.symphony.common.selector.SelectionResult;
import io.symphony.common.selector.LabelSelector.ComparisonOperator;
import tec.units.ri.unit.Units;

public class CompositeSelectorTests {

	@Test
	public void testSelectAND() {

		LabelSelector s1 = LabelSelector.builder().name("foo").operator(ComparisonOperator.EQUALS).value("bar").build();
		LabelSelector s2 = LabelSelector.builder().name("bar").operator(ComparisonOperator.EQUALS).value("foo").build();
		CompositeSelector c1 = CompositeSelector.builder().operator(CompositeSelector.LogicalOperator.AND).selectors(List.of(s1, s2)).build();


		IPoint p1 = QuantityPoint.builder().labels(Map.of("foo", "bar", "bar", "foo")).value(12.3).unit(Units.CELSIUS).build();
		SelectionResult r1 = c1.select(p1);
		assertTrue(r1.isSelected());
		System.out.println(r1.getReason());

		IPoint p2 = QuantityPoint.builder().labels(Map.of("foo", "bar", "bar", "none")).value(12.3).unit(Units.CELSIUS).build();
		SelectionResult r2 = c1.select(p2);
		assertFalse(r2.isSelected());
		System.out.println(r2.getReason());
		
		
	}
	

}
