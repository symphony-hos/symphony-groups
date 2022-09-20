package io.symphony.groups.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "symphony")
@Data
public class GroupsServiceProperties {

	private String name; 

	private List<GroupProperties> groups;
	
	private List<AggregatePropertiesLoader> aggregates;
	
}
