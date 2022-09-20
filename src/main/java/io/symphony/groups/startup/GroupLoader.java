package io.symphony.groups.startup;

import java.util.HashMap;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.symphony.common.selector.Selector;
import io.symphony.common.selector.SelectorPropertiesLoader;
import io.symphony.common.startup.StartupAction;
import io.symphony.groups.data.group.Group;
import io.symphony.groups.data.group.GroupRepo;
import io.symphony.groups.properties.GroupProperties;
import io.symphony.groups.properties.GroupsServiceProperties;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupLoader implements StartupAction {

	private final GroupRepo grpRepo;

	private final GroupsServiceProperties props;

	@Override
	public void run() {
		load();
	}

	private void load() {
		props.getGroups().forEach(p -> load(p));
	}

	private void load(GroupProperties props) {
		String id = props.getId();
		Group group = grpRepo.findById(id).orElse(Group.builder().id(id).build());

		// Copy labels into group
		if (group.getLabels() == null)
			group.setLabels(new HashMap<>());
		for (Entry<String, String> entry : props.getLabels().entrySet())
			group.getLabels().put(entry.getKey(), entry.getValue());

		// Copy selector
		SelectorPropertiesLoader selectorProps = props.getSelector();
		Selector selector = selectorProps.load();
		group.setSelector(selector);

		grpRepo.save(group);
	}

	@Override
	public Integer getOrder() {
		return 100;
	}

}
