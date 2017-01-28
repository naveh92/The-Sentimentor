package main.twitter.stream;

import java.util.HashMap;
import java.util.Map;

import dal.FileManager;
import sentiment.AreaSentiment;
import sentiment.Country;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import view.BrowserMapView;
import view.MapView;

public class TwitterManager {
	private static final String TWITTER_CONSUMER_KEY = "uH53ZuInrhE5LHL7WVlKzdneL";
    private static final String TWITTER_SECRET_KEY = "eChqKSuCx4VQdEXtwmreeIniQVSBnsQHGKT1jX3gMRBvZwNEZH";
    private static final String TWITTER_ACCESS_TOKEN = "773456135810588673-RDyZUmC7q0OyoQE1GOZ3hQ2yG0VnWTF";
    private static final String TWITTER_ACCESS_TOKEN_SECRET = "0Py5Ph0Ahbu8nVcyOkCSYw8LbvwE9GsTGuMQsLInJ3wey";
    
    // <Country Code, Sentiment>
    private static final Map<String, AreaSentiment> areaSentiments = new HashMap<>();
    private static final FileManager fileManager = new FileManager();
    
	public TwitterManager(MapView mapView) {
		
    	ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true);
    	cb.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
    	cb.setOAuthConsumerSecret(TWITTER_SECRET_KEY);
    	cb.setOAuthAccessToken(TWITTER_ACCESS_TOKEN);
    	cb.setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET);  
    	
    	StatusListener statusListener = new MyStatusListener(this, mapView);
		
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream.addListener(statusListener);
        
        // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.sample();
	}
	
	public void addToAreaSentiments(String countryName, String countryCode, Double happiness) {
		if (countryCode != null) {
			synchronized (areaSentiments) {
				// If it doesn't exist - create it
				if (!areaSentiments.containsKey(countryCode)) {
					areaSentiments.put(countryCode, new AreaSentiment(new Country(countryName, countryCode)));
				}
			}
			
			AreaSentiment areaSentiment = areaSentiments.get(countryCode);
				
			// TODO: fix the country name display
//			if (areaSentiment.getCountry().getName().contains("?") && !countryName.contains("?")) {
//				areaSentiment.getCountry().setName(countryName);
//			}
				
			// Add the current tweet to the area sentiments calculations
			areaSentiments.get(countryCode).addToCalculation(happiness);
		}
	}
	
	/**
	 * This function prints the area sentiments and shows them all in a browser map view
	 */
	public static void printAreaSentiments() {
    	int numOfTweets = 0;
    	
    	synchronized (areaSentiments) {
    		for (AreaSentiment sentiment : areaSentiments.values()) {
    			System.out.println(sentiment.getCountry().getName() + ": " + sentiment.getAvgHappiness() + " (" + sentiment.getNumberOfTweets() + " Tweets).");
    			
    			numOfTweets += sentiment.getNumberOfTweets();
    		}
    	}
    	
    	System.out.println("\nAll in all " + numOfTweets + " Tweets.");
    	System.out.println(  "______________________________________");
    	
    	editHtmlFile(numOfTweets);
    }

	private static void editHtmlFile(int numberOfTweets) {			
		fileManager.editHtmlFile(areaSentiments.values(), numberOfTweets);
		BrowserMapView.run();
	}
}
