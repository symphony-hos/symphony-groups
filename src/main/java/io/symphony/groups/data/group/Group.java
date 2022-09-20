package io.symphony.groups.data.group;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.symphony.common.point.data.PointSnapshot;
import io.symphony.common.selector.Selector;
import io.symphony.groups.data.aggregate.QuantityAggregate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({ 
	@Type(value = Group.class, name = "Group"),
	@Type(value = QuantityAggregate.class, name = "QuantityAggregate")
})
@Document(collection = "group")
public class Group implements IGroup {

	@Id
	private String id;
	
	private Map<String, String> labels;
	
	private Selector selector;
	
	private Set<PointSnapshot> points;
	
	@Transient
	private final String type = "Group";
	
	
	@Override
	public void cleanup() {
		Selector selector = getSelector();
		if (selector == null)
			setPoints(Set.of());
		Set<PointSnapshot> filtered = getPoints().stream()
			.filter(i -> selector.select(i.getContent()).isSelected() == true)
			.collect(Collectors.toSet());
		setPoints(filtered);
	}
	
}
