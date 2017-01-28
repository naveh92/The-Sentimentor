package main;

import twitter4j.GeoLocation;

public class StatusCalculation {
	private GeoLocation location;
	private double radius = 0;
	private Double happiness = 0d;
	private String locationName;
	private String locationCode;
	
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public GeoLocation getLocation() {
		return location;
	}
	public void setLocation(GeoLocation location) {
		this.location = location;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public Double getHappiness() {
		return happiness;
	}
	public void setHappiness(Double happiness) {
		this.happiness = happiness;
	}
	
	private StringBuilder stringBuilder = new StringBuilder();
	
	@Override
	public String toString() {
		stringBuilder.delete(0, stringBuilder.length());
		
		stringBuilder
		.append("{")
		
		.append("Happiness = ")
		.append(happiness)
		
		.append(", Country = ")
		.append(locationName)
		
		.append(" (")
		.append(locationCode)
		.append(")")
		
		.append(", Latitude = ")
		.append(location.getLatitude())
		
		.append(", Longitude = ")
		.append(location.getLongitude())
		
		.append(", Radius = ")
		.append(radius)
		
		.append("}");
		
		return stringBuilder.toString();
	}
}
