package main;

import main.twitter.stream.TwitterManager;
import view.MapView;

public class Main {
    public static void main(String[] args) {
    	// Show the map view
    	final MapView mapView = new MapView();
    	
    	// Create the twitter manager
    	final TwitterManager twitterManager = new TwitterManager(mapView); 
    }
}