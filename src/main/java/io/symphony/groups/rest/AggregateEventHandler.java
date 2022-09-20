package io.symphony.groups.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.symphony.common.messages.command.SetQuantity;
import io.symphony.common.messages.command.SetSwitch;
import io.symphony.groups.data.aggregate.Aggregate;
import io.symphony.groups.data.aggregate.AggregateRepo;
import io.symphony.groups.data.aggregate.QuantityAggregate;
import io.symphony.groups.data.aggregate.SwitchAggregate;
import io.symphony.groups.event.CommandPublisher;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AggregateEventHandler {
	
	private final AggregateRepo aggrRepo;
	
	private final CommandPublisher publisher;

	public void handleChange(Aggregate aggr) {
		// Remove unselected points, recalculate and save aggregate
		aggr.cleanup();
		// aggr.calculate();
		aggrRepo.save(aggr);
				
		// Create and publish bulk set command
		if (aggr instanceof SwitchAggregate)
			publisher.publish(SetSwitch.builder()
				.selector(aggr.getSelector())
				.state(((SwitchAggregate) aggr).getState())
				.build());
		else if (aggr instanceof QuantityAggregate) {
			publisher.publish(SetQuantity.builder()
				.selector(aggr.getSelector())
				.value(((QuantityAggregate) aggr).getValue())
				.build());
		}
	}
	
}
