package view.waypoint;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WaypointsPainter extends WaypointPainter<JXMapViewer> {
	
    public void setWaypoints(List<? extends Waypoint> waypoints) {
        super.setWaypoints(new HashSet<Waypoint>(waypoints));
    }
    
    public void addWaypoint(Waypoint waypoint) {
    	Set<Waypoint> waypoints = super.getWaypoints();
    	waypoints.add(waypoint);
    	
    	super.setWaypoints(waypoints);
    }
}
