package io.symphony.groups.event;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.symphony.common.messages.event.PointUpdate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PointSubscriber {

	private Logger logger = LoggerFactory.getLogger(PointSubscriber.class);

	private final List<PointListener> listeners;

	@Bean
	public Consumer<PointUpdate> processPoint() {
		return (point) -> {
			logger.trace("Received point update: {}", point);
			if (listeners != null)
				listeners.forEach(l -> l.pointUpdate(point));
		};
	}

}