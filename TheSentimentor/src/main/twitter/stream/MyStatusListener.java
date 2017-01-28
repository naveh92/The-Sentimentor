package main.twitter.stream;

import main.Constants;
import main.StatusCalculation;
import sentiment.SentimentCalculator;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import view.MapView;
import view.waypoint.FancyWaypoint;
import twitter4j.StatusListener;

public class MyStatusListener implements StatusListener {
	TwitterManager context;
	MapView mapView;
	
	public MyStatusListener(TwitterManager context, MapView mapView) {
		this.mapView = mapView;
		this.context = context;
	}
	
	@Override
	public void onStatus(Status status) {
//		String language = status.getUser().getLang();
		
		Place place = status.getPlace();
		StatusCalculation statusCalc = null;
		
		if (place != null) {
			String countryName = place.getCountry();
			String countryCode = place.getCountryCode();
			
			if (countryCode.equals("RU") || countryCode.equals("JP")) {
				System.out.println();
			}
			
			statusCalc = new StatusCalculation();
			statusCalc.setLocationName(countryName);
			statusCalc.setLocationCode(countryCode);
			
			// Check if the geo location exists (if place is null then geo-location is null)
			if (status.getGeoLocation() != null) {
				statusCalc.setLocation(status.getGeoLocation());

				// Set a default radius
				statusCalc.setRadius(Constants.DEFAULT_RADIUS);
			}
			else {
				// Get the bounding box coordinates
				GeoLocation[][] coordinates = place.getBoundingBoxCoordinates();
			
				// Calculate the location and radius using the coordinates
				// NOTE: This function also sets the location and radius into statusCalc.
				calculateLocationData(statusCalc, coordinates);
			}
			
			// Calculate the happiness from the status text
			Double happiness = calculateHappiness(status.getText());
			statusCalc.setHappiness(happiness);
			
			System.out.println("");
			System.out.println(status.getText());
			if (statusCalc != null) {
				System.out.println(statusCalc);
				
				// The maximum size on the map will be Constants.RADIUS_RESOLUTION
				if (statusCalc.getRadius() > 1) {
					statusCalc.setRadius(1);
				}
				
				context.addToAreaSentiments(countryName, countryCode, happiness);
				
				// Add the current tweet to the map
				mapView.addWayPoint(new FancyWaypoint(status.getText() + " - (" + statusCalc.getHappiness() + ")",
													  statusCalc.getHappiness(),
												      statusCalc.getLocation().getLatitude(), 
						                              statusCalc.getLocation().getLongitude(),
						                              statusCalc.getRadius()));
			}
			System.out.println("__________________________________________________________________________________________________________________________________________________________");
		}
		else {
			
		}
		
//		System.out.println("");
//		System.out.println(status.getText());
//		if (statusCalc != null) {
//			System.out.println("(Tweeted from " + statusCalc.toString() + ")");
//		}
//		System.out.println("__________________________________________________________________________________________________________________________________________________________");
	}
	
	private Double calculateHappiness(String text) {
		return SentimentCalculator.getInstance().calculateSentiment(text);
	}

	/**
	 * This function calculates a specific location and radius of a Status.
	 * @param statusCalc - An "out" parameter. We use it only for setting the calculated data.
	 * @param coordinates - The status' bounding box coordinates.
	 */
	private void calculateLocationData(StatusCalculation statusCalc, GeoLocation[][] coordinates) {
		double sumLatitude = 0, sumLongitude = 0;
		double minLat = Double.POSITIVE_INFINITY, minLong = Double.POSITIVE_INFINITY;
		double maxLat = Double.NEGATIVE_INFINITY, maxLong = Double.NEGATIVE_INFINITY;
		int coordsCount = 0;
		
		for (int i=0; i<coordinates.length; i++) {
			for (int j=0; j<coordinates[i].length; j++) {
				// Get the location data
				double latitude = coordinates[i][j].getLatitude();
				double longitude = coordinates[i][j].getLongitude();  
				
				sumLatitude += latitude;
				sumLongitude += longitude;
				
				if (latitude < minLat) {
					minLat = latitude;
				}
				if (latitude > maxLat) {
					maxLat = latitude;
				}
				if (longitude < minLong) {
					minLong = longitude;
				}
				if (longitude > maxLong) {
					maxLong = longitude;
				}
				
				coordsCount++;
			}
		}
		
		// Make sure the values were initialized, otherwise we would get a huge radius
		if (minLat != Double.POSITIVE_INFINITY && minLong != Double.POSITIVE_INFINITY) {
			// Get the farthest points from each other
			double radius = Math.max(maxLat - minLat, maxLong - minLong);
			
			statusCalc.setRadius(radius);
		}
		
		statusCalc.setLocation(new GeoLocation(sumLatitude / coordsCount,
											   sumLongitude / coordsCount));
	}
	
	@Override
	public void onTrackLimitationNotice(int arg0) {}
	@Override
	public void onException(Exception arg0) {arg0.printStackTrace();}
	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {}
	@Override
	public void onScrubGeo(long arg0, long arg1) {}
	@Override
	public void onStallWarning(StallWarning arg0) {}
}
