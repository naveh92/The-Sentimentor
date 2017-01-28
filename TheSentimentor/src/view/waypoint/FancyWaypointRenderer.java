package view.waypoint;

/*
 * WaypointRenderer.java
 *
 * Created on March 30, 2006, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import main.Constants;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

/**
 * A fancy waypoint painter
 */
public class FancyWaypointRenderer implements WaypointRenderer
{
	private final String imgUrl = "white_circle2.png";
//	private final String imgUrl = "waypoint_white.png";
	
	private final Map<Color, BufferedImage> map = new HashMap<Color, BufferedImage>();
	
	private final Font font = new Font("Lucida Sans", Font.BOLD, 10);
	
	private BufferedImage origImage;

	/**
	 * Uses a default waypoint image
	 */
	public FancyWaypointRenderer()
	{
		URL resource = getClass().getResource(imgUrl);

		try
		{
			origImage = ImageIO.read(resource);
		}
		catch (Exception ex)
		{
			System.out.println("couldn't read " + imgUrl);
		}
	}

	private BufferedImage convert(BufferedImage loadImg, Color newColor)
	{
		int w = loadImg.getHeight();
		int h = w;
		
		BufferedImage imgOut = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		BufferedImage imgColor = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = imgColor.createGraphics();
		g.setColor(newColor);
		g.fillRect(0, 0, w+1, h+1);
		g.dispose();

		Graphics2D graphics = imgOut.createGraphics();
		graphics.drawImage(loadImg, 0, 0, null);
		graphics.setComposite(MultiplyComposite.Default);
		graphics.drawImage(imgColor, 0, 0, null);
		graphics.dispose();
		
		return imgOut;
	}

	@Override
	public boolean paintWaypoint(Graphics2D g, JXMapViewer viewer, Waypoint w)
	{
		FancyWaypoint waypoint = (FancyWaypoint)w;
		
		g = (Graphics2D)g.create();
		
		if (origImage == null)
			return false;
		
		BufferedImage myImg = map.get(waypoint.getColor());
		
		if (myImg == null)
		{
			myImg = convert(origImage, waypoint.getColor());
			map.put(waypoint.getColor(), myImg);
		}

//		Point2D point = MapView.jxMapKit.getMainMap().getTileFactory().geoToPixel(waypoint.getPosition(), MapView.jxMapKit.getMainMap().getZoom());
		
//		Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());
//		int x = (int)point.getX();
//		int y = (int)point.getY();
		
		int x = (int) waypoint.getPosition().getLatitude()/Constants.RADIUS_RESOLUTION;
		int y = (int) waypoint.getPosition().getLongitude()/Constants.RADIUS_RESOLUTION;
		
		int zoomFactor = ((Constants.MAX_ZOOM+1)-viewer.getZoom());
		zoomFactor *= (((double)zoomFactor)/2);
		
		// Make sure it's at least 1, so that we can see all the waypoints
		if (zoomFactor == 0) {
			zoomFactor++;
		}
		
		int imgRadius = (int) (Constants.RADIUS_RESOLUTION*waypoint.getRadius()*zoomFactor);
		
//		g.drawImage(myImg, x -(myImg.getWidth()/2), y-(myImg.getHeight()/2), null);
		g.drawImage(myImg, x-(imgRadius/2), y-(imgRadius/2), imgRadius, imgRadius, null);
		
		String label = waypoint.getLabel();
	
		g.setFont(font);

		FontMetrics metrics = g.getFontMetrics();
		int tw = metrics.stringWidth(label);
		int th = 1 + metrics.getAscent();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g.drawString(label, x - tw / 2, y + th -(int)(1f*myImg.getHeight()));
		g.drawString(label, x - tw/2, y - (imgRadius/2));
		
		g.dispose();
		
		return true;
	}
}
