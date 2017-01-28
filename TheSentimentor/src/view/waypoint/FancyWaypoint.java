package view.waypoint;

import java.awt.Color;

import main.Constants;

import org.jdesktop.swingx.mapviewer.DefaultWaypoint;

/**
 * A Waypoint that also has a color and a label
 */
public class FancyWaypoint extends DefaultWaypoint
{
	private final String realLabel; 
	private String label = "";
	private final Color color;
	private double radius;

	/**
	 * @param label the text
	 * @param color the color
	 * @param coord the coordinate
	 */
	public FancyWaypoint(String label, Double happiness, double latitude, double longitude, double radius)
	{
		super(latitude, longitude);
		
		this.realLabel = label;
		this.radius = radius;
		
		int r = 255 - (int)(((happiness-1)*255) / Constants.MAX_HAPPINESS);
		int g = (int)(((happiness-1)*255) / Constants.MAX_HAPPINESS);
		int b = 0;
		
		// Create an RGB color according to the happiness:
		// Happy = green, Sad = red.
		this.color = new Color(r, g, b);
	}

	/**
	 * @return the label text
	 */
	public String getLabel()
	{
		return label;
	}
	
	public void showLabel() 
	{
		label = realLabel;
	}
	
	public void hideLabel()
	{
		label = "";
	}

	/**
	 * @return the color
	 */
	public Color getColor()
	{
		return color;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
}