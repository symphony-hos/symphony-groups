package io.symphony.groups.data.aggregate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.symphony.common.point.data.Access;
import io.symphony.common.point.IPoint;
import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.PointSnapshot;
import io.symphony.common.selector.Selector;
import io.symphony.groups.data.group.IGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({ 
	@Type(value = QuantityAggregate.class, name = "QuantityAggregate"),
	@Type(value = StateAggregate.class, name = "StateAggregate")
})
@Document(collection = "aggregate")
public abstract class Aggregate implements IGroup, IPoint {

	@Id
	private String id;
	
	// Labels set by the user via properties or API
	private Map<String, String> labels;

	// Labels calculated from selected points
	@JsonIgnore
	private Map<String, String> pointLabels;
	
	private Selector selector;
	
	private Set<PointSnapshot> points;
	
	private Set<Access> access;

	
	public Map<String, String> getLabels() {
		if (pointLabels == null)
			pointLabels = new HashMap<>();
		Map<String, String> labels = new HashMap<>(pointLabels);
		labels.putAll(this.labels);
		return labels;
	}
	
	
	public abstract Point asPoint();
	
	
	@Transient
	public abstract String getType();
	
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
	
	public void calculate() {
		if (getPoints() == null)
			 setPoints(new HashSet<>());
		
		// Set aggregates access values. Aggregate access equals the lowest common
		// access modes available to points
		Set<Access> aggrAccess = new HashSet<>(Set.of(Access.values()));
		for (PointSnapshot point: getPoints()) {
			for (Access access: Set.of(Access.values())) {
				if (!point.getContent().getAccess().contains(access))
					aggrAccess.remove(access);
			}
		}
		setAccess(aggrAccess);
		
		// Set pointLabels. Find all labels that all point share

		List<Point> allPoints = getPoints().stream().map(s -> s.getContent()).collect(Collectors.toList());
		
		List<String> allLabelKeys = allPoints.stream()
			.map(p -> p.getLabels())
			.flatMap(m -> m.entrySet().stream())
			.map(e -> e.getKey())
			.collect(Collectors.toList());
		
		Map<String, Set<String>> combinedLabels = new HashMap<>();
		for (String labelKey: allLabelKeys) {
			if (!combinedLabels.containsKey(labelKey))
				combinedLabels.put(labelKey, new HashSet<>());
			allPoints.stream() 
				.filter(p -> p.getLabels().containsKey(labelKey))
				.forEach(p -> combinedLabels.get(labelKey).add(p.getLabels().get(labelKey)));
		}
		
		pointLabels = combinedLabels.entrySet().stream()
		  .filter(e -> e.getValue().size() == 1)
		  .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().iterator().next()));		
	}
	
}
