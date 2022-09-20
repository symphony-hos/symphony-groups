package io.symphony.groups.data.group;

import java.util.Map;
import java.util.Set;

import io.symphony.common.point.data.PointSnapshot;
import io.symphony.common.selector.Selector;

public interface IGroup {

	String getId();

	void setId(String id);

	
	Map<String, String> getLabels();

	void setLabels(Map<String, String> labels);

	
	Selector getSelector();

	void setSelector(Selector selector);

	
	Set<PointSnapshot> getPoints();

	void setPoints(Set<PointSnapshot> points);

	
	public void cleanup();
	
}