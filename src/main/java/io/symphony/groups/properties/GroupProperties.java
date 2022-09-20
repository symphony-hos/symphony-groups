package io.symphony.groups.properties;

import java.util.Map;

import io.symphony.common.selector.SelectorPropertiesLoader;
import lombok.Data;

@Data
public class GroupProperties {

	private String id;
	
	private Map<String, String> labels;
	
	private SelectorPropertiesLoader selector;
	
}
