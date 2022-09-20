package io.symphony.groups.event;

import io.symphony.common.messages.event.PointUpdate;

public interface PointListener {

	public void pointUpdate(PointUpdate point);
	
}
