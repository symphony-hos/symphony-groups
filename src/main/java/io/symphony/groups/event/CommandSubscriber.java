package io.symphony.groups.event;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.symphony.common.messages.command.Command;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommandSubscriber {

	private Logger logger = LoggerFactory.getLogger(CommandSubscriber.class);
	
	private final CommandExecutor executor;

	@Bean
	public Consumer<Command> processCommand() {
		return (command) -> {
			logger.debug("Received command: {}", command);
			executor.execute(command);
		};
	}

}