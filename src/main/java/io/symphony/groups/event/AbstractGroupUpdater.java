package io.symphony.groups.event;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.symphony.common.messages.event.PointUpdate;
import io.symphony.common.point.data.Access;
import io.symphony.common.point.data.Point;
import io.symphony.common.point.data.PointSnapshot;
import io.symphony.common.selector.SelectionResult;
import io.symphony.common.selector.Selector;
import io.symphony.groups.data.group.IGroup;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractGroupUpdater<T extends IGroup> implements PointListener {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	

	@Override
	public void pointUpdate(PointUpdate event) {
		logger.trace("Processing point update {}", event);
		Point point = event.getPoint();

		Iterable<T> allGroups = getAllGroups();
		updateGroups(allGroups, point, event);
		saveAllGroups(allGroups);
	}
	
	protected abstract Iterable<T> getAllGroups();
	
	protected abstract Set<T> saveAllGroups(Iterable<T> groups);
		
	protected void updateGroups(Iterable<T> groups, Point point, PointUpdate event) {
		groups.forEach(g -> updateGroup(g, point, event.getExtension(), event.getIdentifier()));
	}
	
	protected void updateGroup(T grp, Point point, String ext, String id) {
		if (point.getAccess() == null || !point.getAccess().contains(Access.READ)) {
			logger.debug("Point {}/{} is not readible. Ignoring.", ext, id);
			return;
		}
		
		logger.trace("Running update for group {} triggered by new event for point {}/{}", grp.getId(), ext, id);
		if (grp.getPoints() == null)
			grp.setPoints(new HashSet<>());
		
		Selector s = grp.getSelector();
		if (s != null) {
			SelectionResult result = s.select(point);
			boolean selected = result.isSelected();
			logger.trace("Checking if selector of group {} selects point. Result {}", grp.getId(), selected);
			
			if (selected == false)
				logger.trace("Selector denies point. Reason(s): " + result.getReason());
			
			PointSnapshot newInfo = PointSnapshot.from(id, point);
			logger.trace("Created new PointInfo to store with Group: {}", newInfo);

			boolean containedBefore = grp.getPoints().contains(newInfo);
			grp.getPoints().remove(newInfo);
			
			if (selected == true) {
				if (containedBefore == true) {
					logger.info("Updating point {}:{} in group {}", ext, id, grp.getId());
				} else {
					logger.info("Adding point {}:{} to group {}", ext, id, grp.getId());
				}
				grp.getPoints().add(newInfo);
			} else {
				if (containedBefore == true) {
					logger.info("Removing point {}:{} from group {}", ext, id, grp.getId());
				}	
			}
		}
	}
	
	protected Set<T> toSet(Iterable<T> iter) {
		return StreamSupport.stream(iter.spliterator(), false)
			.collect(Collectors.toSet());
	}

}
