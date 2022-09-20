package io.symphony.groups.event;

import java.util.LinkedList;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.symphony.common.messages.event.PointUpdate;
import io.symphony.groups.data.aggregate.Aggregate;
import io.symphony.groups.data.aggregate.AggregateRepo;
import io.symphony.groups.properties.GroupsServiceProperties;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PointPublisher {
	
	private Logger logger = LoggerFactory.getLogger(PointPublisher.class);
	
	private final AggregateRepo repo;
	
	private final GroupsServiceProperties props;
	
	private LinkedList<PointUpdate> queue = new LinkedList<>();

	public void publish(Aggregate aggregate) {
		if (aggregate != null)
			queue.add(toEvent(aggregate));
	}
	
	public void publishAll() {
		logger.debug("Publishing all points");
		int i = 0;
		for (Aggregate p: repo.findAll()) {
			logger.debug("Publishing point: {}", p.getId());
			publish(p);
			i++;
		}
		logger.debug("Queued {} points for publishing", i);
	}

	private PointUpdate toEvent(Aggregate aggregate) {
		return PointUpdate.builder()
			.extension(props.getName())
			.identifier(aggregate.getId())
			.point(aggregate.asPoint())
			.build();
	}
	
	@Bean
	public Supplier<PointUpdate> publishPoint() {
		return () -> {
			if (queue.size() > 0)
				return queue.removeFirst();
			return null;
		};
	}

}