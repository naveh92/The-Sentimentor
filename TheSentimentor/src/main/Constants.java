package main;

public class Constants {
	public static final int MAX_ZOOM = 15;
	public static double DEFAULT_RADIUS = 0.1;
	public static int MAX_HAPPINESS = 4;
	public static int RADIUS_RESOLUTION = 50;
	public static String ORIGINAL_MAP_URL = "original_map.html";
	public static String NEW_MAP_URL = "new_map.html";
	
	private static int numberOfDecimalPointsToDisplay = 2;
	
	/**
	 * This function takes a double value and returns it trimmed to numberOfDecimalPointsToDisplay decimal points 
	 */
	public static Double decimalTrim(Double value) {
		// This code section cuts the decimal points and only leaves Constants.nubmerOfDecimalPointsToDisplay.
		int multiplyer = (int) Math.pow(10, numberOfDecimalPointsToDisplay);
		int intValueMultiplied = (int) (multiplyer * value);
		
		return (double) ((double)intValueMultiplied / multiplyer);
	}
}
