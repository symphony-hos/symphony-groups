package io.symphony.groups.rest.group;

import java.util.Map;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.symphony.common.selector.Selector;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GroupModel extends RepresentationModel<GroupModel> {

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String id;
	
	private Map<String, String> labels;

	private Selector selector;
	
}
