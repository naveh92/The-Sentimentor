package authentication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class AuthenticationMain {
	@SuppressWarnings("unused")
	private static String encodedCredentials;
	private static String bearerToken;
	private static final String TWITTER_CONSUMER_KEY = "uH53ZuInrhE5LHL7WVlKzdneL";
    private static final String TWITTER_SECRET_KEY = "eChqKSuCx4VQdEXtwmreeIniQVSBnsQHGKT1jX3gMRBvZwNEZH";
    
	
    public static void main(String[] args) {
    	encodedCredentials = encodeKeys(TWITTER_CONSUMER_KEY, TWITTER_SECRET_KEY);
    	
    	try 
    	{
			bearerToken = requestBearerToken("https://api.twitter.com/oauth2/token");
		}
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
    	
    	try 
    	{
			System.out.println(fetchTimelineTweet("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2"));
		} 
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	// Encodes the consumer key and secret to create the basic authorization key
	private static String encodeKeys(String consumerKey, String consumerSecret) {
	    try {
	        String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
	        String encodedConsumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");
	        String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;
	        String encodedString = DatatypeConverter.printBase64Binary(fullKey.getBytes());
	        return new String(encodedString);  
	    }
	    catch (UnsupportedEncodingException e) {
	        return new String();
	    }
	}
	
	// Constructs the request for requesting a bearer token and returns that token as a string
		private static String requestBearerToken(String endPointUrl) throws IOException {
		    HttpsURLConnection connection = null;
		    String encodedCredentials = encodeKeys("<consumerkey>","<consumersecret>");
		    try {
		        URL url = new URL(endPointUrl); 
		        connection = (HttpsURLConnection) url.openConnection();           
		        connection.setDoOutput(true);
		        connection.setDoInput(true); 
		        connection.setRequestMethod("POST"); 
		        connection.setRequestProperty("Host", "api.twitter.com");
		        connection.setRequestProperty("User-Agent", "Your Program Name");
		        connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
		        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"); 
		        connection.setRequestProperty("Content-Length", "29");
		        connection.setUseCaches(false);
		             
		        writeRequest(connection, "grant_type=client_credentials");
		             
		        // Parse the JSON response into a JSON mapped object to fetch fields from.
		        JSONObject obj = (JSONObject)JSONValue.parse(readResponse(connection));
		             
		        if (obj != null) {
		            String tokenType = (String)obj.get("token_type");
		            String token = (String)obj.get("access_token");
		         
		            return ((tokenType.equals("bearer")) && (token != null)) ? token : "";
		        }
		        return new String();
		    }
		    catch (MalformedURLException e) {
		        throw new IOException("Invalid endpoint URL specified.", e);
		    }
		    finally {
		        if (connection != null) {
		            connection.disconnect();
		        }
		    }
		}
		
		// Writes a request to a connection
			private static boolean writeRequest(HttpsURLConnection connection, String textBody) {
			    try {
			        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			        wr.write(textBody);
			        wr.flush();
			        wr.close();
			             
			        return true;
			    }
			    catch (IOException e) { return false; }
			}
			     
			     
			// Reads a response for a given connection and returns it as a string.
			private static String readResponse(HttpsURLConnection connection) {
			    try {
			        StringBuilder str = new StringBuilder();
			             
			        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			        String line = "";
			        while((line = br.readLine()) != null) {
			            str.append(line + System.getProperty("line.separator"));
			        }
			        return str.toString();
			    }
			    catch (IOException e) { return new String(); }
			}

			
			// Fetches the first tweet from a given user's timeline
				private static String fetchTimelineTweet(String endPointUrl) throws IOException {
				    HttpsURLConnection connection = null;
				                 
				    try {
				        URL url = new URL(endPointUrl); 
				        connection = (HttpsURLConnection) url.openConnection();           
				        connection.setDoOutput(true);
				        connection.setDoInput(true); 
				        connection.setRequestMethod("GET"); 
				        connection.setRequestProperty("Host", "api.twitter.com");
				        connection.setRequestProperty("User-Agent", "Your Program Name");
				        connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
				        connection.setUseCaches(false);
				             
				             
				        // Parse the JSON response into a JSON mapped object to fetch fields from.
				        JSONArray obj = (JSONArray)JSONValue.parse(readResponse(connection));
				             
				        if (obj != null) {
				            String tweet = ((JSONObject)obj.get(0)).get("text").toString();
				 
				            return (tweet != null) ? tweet : "";
				        }
				        return new String();
				    }
				    catch (MalformedURLException e) {
				        throw new IOException("Invalid endpoint URL specified.", e);
				    }
				    finally {
				        if (connection != null) {
				            connection.disconnect();
				        }
				    }
				}
			
			
			
			
			
			
			
			
			
			
			
    
//    private static void guh() {
//        ConfigurationBuilder cb = new ConfigurationBuilder();
//        cb.setDebugEnabled(true)
//            .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
//            .setOAuthConsumerSecret(TWITTER_SECRET_KEY)
//            .setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
//            .setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET);
//        TwitterFactory tf = new TwitterFactory(cb.build());
//        Twitter twitter = tf.getInstance();
//        try {
//            Query query = new Query("MrEdPanama");
//            QueryResult result;
//            do {
//                result = twitter.search(query);
//                List<Status> tweets = result.getTweets();
//                for (Status tweet : tweets) {
//                    System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
//                }
//            } while ((query = result.nextQuery()) != null);
//            System.exit(0);
//        } catch (TwitterException te) {
//            te.printStackTrace();
//            System.out.println("Failed to search tweets: " + te.getMessage());
//            System.exit(-1);
//        }
//    }
}