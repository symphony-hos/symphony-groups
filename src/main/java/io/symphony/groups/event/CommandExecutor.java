package io.symphony.groups.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.symphony.common.messages.command.Command;
import io.symphony.common.messages.command.PublishAll;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommandExecutor {

	private final PointPublisher publisher;

	public void execute(Command command) {
		if (command instanceof PublishAll)
			publisher.publishAll();
	}

}
