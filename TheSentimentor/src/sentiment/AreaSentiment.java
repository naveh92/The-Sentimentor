package sentiment;

import main.Constants;

public class AreaSentiment {
	private Country country;
	private Double avgHappiness;
	private int numberOfTweets = 0;
	
	public AreaSentiment(Country country) {
		this.country = country;
	}
	
	public Country getCountry() {
		return country;
	}
	
	public Double getAvgHappiness() {
		return Constants.decimalTrim(avgHappiness);
	}
	
	public int getNumberOfTweets() {
		return numberOfTweets;
	}

	public void addToCalculation(Double tweetHappiness) {
		numberOfTweets++;
		
		if (numberOfTweets == 1) {
			avgHappiness = tweetHappiness;
		}
		else {
			Double oldAvg = avgHappiness;
			Double oldSum = oldAvg * (numberOfTweets-1);
			
			oldSum += tweetHappiness;
			
			avgHappiness = oldSum / numberOfTweets;
		}
	}
}
