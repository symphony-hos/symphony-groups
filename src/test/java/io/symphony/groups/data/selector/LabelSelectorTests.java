package io.symphony.groups.data.selector;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.symphony.common.point.IPoint;
import io.symphony.common.point.data.quantity.QuantityPoint;
import io.symphony.common.selector.LabelSelector;
import io.symphony.common.selector.SelectionResult;
import io.symphony.common.selector.LabelSelector.ComparisonOperator;
import tec.units.ri.unit.Units;

public class LabelSelectorTests {

	@Test
	public void testSelectEquals() {

		IPoint p1 = QuantityPoint.builder().labels(Map.of("foo", "bar")).value(12.3).unit(Units.CELSIUS).build();
		IPoint p2 = QuantityPoint.builder().labels(Map.of("bar", "foo")).value(12.3).unit(Units.CELSIUS).build();
		
		LabelSelector s = LabelSelector.builder().name("foo").operator(ComparisonOperator.EQUALS).value("bar").build();

		SelectionResult r1 = s.select(p1);
		assertTrue(r1.isSelected());
		System.out.println(r1.getReason());
	
		SelectionResult r2 = s.select(p2);
		assertFalse(r2.isSelected());
		System.out.println(r2.getReason());
	}
	
	@Test
	public void testSelectNotEquals() {

		IPoint p1 = QuantityPoint.builder().labels(Map.of("foo", "bar")).value(12.3).unit(Units.CELSIUS).build();
		IPoint p2 = QuantityPoint.builder().labels(Map.of("foo", "foo")).value(12.3).unit(Units.CELSIUS).build();
		
		LabelSelector s = LabelSelector.builder().name("foo").operator(ComparisonOperator.NOT_EQUALS).value("bar").build();

		SelectionResult r1 = s.select(p1);
		assertFalse(r1.isSelected());
		System.out.println(r1.getReason());
	
		SelectionResult r2 = s.select(p2);
		assertTrue(r2.isSelected());
		System.out.println(r2.getReason());
	}

}
