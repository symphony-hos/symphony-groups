package io.symphony.groups.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.symphony.common.messages.command.PublishAll;
import io.symphony.common.startup.StartupAction;
import io.symphony.groups.event.CommandPublisher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateRequester implements StartupAction {

	private final CommandPublisher publisher;

	@Override
	public void run() {
		publisher.publish(PublishAll.builder().build());
	}

	@Override
	public Integer getOrder() {
		return 500;
	}

}
