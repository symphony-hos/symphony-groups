package io.symphony.groups.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalPointId {

	private String extension;
	
	private String pointId;
	
	
	public String toString() {
		return extension + "/" + pointId;
	}
	
}
