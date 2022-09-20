package io.symphony.groups.event;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.symphony.common.messages.event.PointUpdate;
import io.symphony.common.point.data.Point;
import io.symphony.groups.data.aggregate.Aggregate;
import io.symphony.groups.data.aggregate.AggregateRepo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AggregateUpdater extends AbstractGroupUpdater<Aggregate> {
	
	private final AggregateRepo repo;

	@Override
	protected Iterable<Aggregate> getAllGroups() {
		return repo.findAll();
	}

	@Override
	protected Set<Aggregate> saveAllGroups(Iterable<Aggregate> groups) {
		return toSet(repo.saveAll(groups));
	}
	
	@Override
	public void pointUpdate(PointUpdate event) {
		Point point = event.getPoint();		
		Iterable<Aggregate> allAggr = getAllGroups();
		updateGroups(allAggr, point, event);
		allAggr.forEach(aggr -> aggr.calculate());
		saveAllGroups(allAggr);
	}
	

}
