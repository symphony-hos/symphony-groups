package io.symphony.groups.rest.aggregate.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.symphony.common.point.data.Access;
import io.symphony.common.selector.Selector;
import io.symphony.groups.data.aggregate.Aggregate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({ 
	@Type(value = QuantityAggregateModel.class, name = "QuantityAggregate"), 
	@Type(value = SwitchAggregateModel.class, name = "SwitchAggregate"), 
	@Type(value =  ContactAggregateModel.class, name = "ContactAggregate"), 
})
public abstract class AggregateModel<T extends RepresentationModel<? extends T>, E extends Aggregate> extends RepresentationModel<T> {

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String id;
	
	@JsonIgnore
	private Class<? extends E> entityType;
	
	private Map<String, String> labels;

	private Selector selector;
	
	private Set<Access> access;
	
	public abstract String getType();
	
	
	public AggregateModel(Class<? extends E> entityType) {
		this(entityType, null);
	}
	
	public AggregateModel(Class<? extends E> entityType, E aggr) {
		this.entityType = entityType;
		if (aggr != null) {
			this.id = aggr.getId();
			this.labels = aggr.getLabels();
			this.selector = aggr.getSelector();
			this.access = aggr.getAccess();
		}
	}
		
	public void updateIfSupported(Aggregate aggr, boolean onlyNonNull) {
		if (entityType.isAssignableFrom(aggr.getClass())) 
			update(entityType.cast(aggr), onlyNonNull);
	}
	
	public void update(E aggr, boolean onlyNonNull) {
		// Labels
		if (!onlyNonNull || (onlyNonNull && labels != null)) {
			if (labels == null)
				labels = new HashMap<>();
			if (aggr.getLabels() == null) 
				aggr.setLabels(new HashMap<>());
			aggr.setLabels(labels);
		} 
		
		// Selector
		if (!onlyNonNull || (onlyNonNull && selector != null)) {
			aggr.setSelector(selector);
		}
	}

}
