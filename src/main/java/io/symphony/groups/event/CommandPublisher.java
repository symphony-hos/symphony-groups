package io.symphony.groups.event;

import java.util.LinkedList;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.symphony.common.messages.command.Command;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommandPublisher {
	
	private LinkedList<Command> queue = new LinkedList<>();

	public void publish(Command command) {
		if (command != null)
			queue.add(command);
	}
	
	@Bean
	public Supplier<Command> publishCommand() {
		return () -> {
			if (queue.size() > 0) {
				return queue.removeFirst();
			}
			return null;
		};
	}

}