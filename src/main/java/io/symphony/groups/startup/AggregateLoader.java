package io.symphony.groups.startup;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.symphony.common.startup.StartupAction;
import io.symphony.groups.data.aggregate.Aggregate;
import io.symphony.groups.data.aggregate.AggregateRepo;
import io.symphony.groups.properties.AggregatePropertiesLoader;
import io.symphony.groups.properties.GroupsServiceProperties;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AggregateLoader implements StartupAction {
	
	private final AggregateRepo aggRepo;
	
	private final GroupsServiceProperties props;
	
	@Override
	public void run() {
		load();
	}
	
	
	private void load() {
		props.getAggregates().forEach(p -> load(p));
	}
	
	private void load(AggregatePropertiesLoader props) {
		String id = props.getId();
	
		Optional<Aggregate> aggr = aggRepo.findById(id);
		
		if (aggr.isPresent())
			aggRepo.delete(aggr.get());
		
		Aggregate newAggr = props.load();		
		aggRepo.save(newAggr);
	}
	
	

	@Override
	public Integer getOrder() {
		return 100;
	}

}
