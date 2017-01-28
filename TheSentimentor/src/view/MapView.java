package view;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import main.Constants;
import main.twitter.stream.TwitterManager;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.GeoPosition;

import view.waypoint.FancyWaypoint;
import view.waypoint.FancyWaypointRenderer;
import view.waypoint.WaypointsPainter;

public class MapView extends JFrame {
	private static List<FancyWaypoint> waypoints = new ArrayList<>();
	public static JXMapKit jxMapKit = new JXMapKit();
	private static WaypointsPainter painter = new WaypointsPainter();
	
	public void addWayPoint(FancyWaypoint waypoint) {
		synchronized (waypoints) {
			waypoints.add(waypoint);
		}
		
		painter.setWaypoints(waypoints);
		jxMapKit.updateUI();
	}
	
    public MapView() {
        jxMapKit.setDefaultProvider(JXMapKit.DefaultProviders.OpenStreetMaps);
        painter.setWaypoints(waypoints);
        jxMapKit.getMainMap().setOverlayPainter(painter);
        jxMapKit.setMiniMapVisible(false);
        
        jxMapKit.getMainMap().addMouseMotionListener(new MouseAdapter() {
   		 
            @Override
            public void mouseMoved(MouseEvent me) {
                Point2D gp_pt = null;
 
                synchronized (waypoints) {
	                for (FancyWaypoint waypoint : waypoints) {
	                	GeoPosition position = waypoint.getPosition();
	                    // convert to world bitmap
	                    gp_pt = jxMapKit.getMainMap().getTileFactory().geoToPixel(position, jxMapKit.getMainMap().getZoom());
	 
	                    // convert to screen
	                    Rectangle rect = jxMapKit.getMainMap().getViewportBounds();
	                    Point converted_gp_pt = new Point((int) gp_pt.getX() - rect.x, (int) gp_pt.getY() - rect.y);
	                    
	            		int zoomFactor = ((Constants.MAX_ZOOM+1)-jxMapKit.getMainMap().getZoom());
	            		zoomFactor *= (((double)zoomFactor)/2);
	            		
	            		// Make sure it's at least 1, so that we can see all the waypoints
	            		if (zoomFactor == 0) {
	            			zoomFactor++;
	            		}
	                    
	                    // Check if near the mouse
	                    if (converted_gp_pt.distance(me.getPoint()) < waypoint.getRadius()/2 * Constants.RADIUS_RESOLUTION * (zoomFactor)) {
	                    	waypoint.showLabel();
	                    	jxMapKit.updateUI();
	                    }
	                    else {
	                    	waypoint.hideLabel();
	                    	jxMapKit.updateUI();
	                    }
	                    
	//                    waypoint.showLabel();
//	                    System.out.println(converted_gp_pt.distance(me.getPoint()));
	                }
                }
            }
});
        
        jxMapKit.getMainMap().addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				TwitterManager.printAreaSentiments();
			}
		});
        
		painter.setRenderer(new FancyWaypointRenderer());
		jxMapKit.getMainMap().setOverlayPainter(painter);
        
        add(jxMapKit);
        setSize(1500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }
}