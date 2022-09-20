package io.symphony.groups.event;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.symphony.groups.data.group.Group;
import io.symphony.groups.data.group.GroupRepo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupUpdater extends AbstractGroupUpdater<Group> {

	private final GroupRepo repo;

	@Override
	protected Iterable<Group> getAllGroups() {
		return repo.findAll();
	}

	@Override
	protected Set<Group> saveAllGroups(Iterable<Group> groups) {
		return toSet(repo.saveAll(groups));
	}

}
