import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class Main {
	
	public static void main(String[] args) {
		
	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true).setOAuthConsumerKey("4A2NJHJrzD3eCtzitoJyqxDFe")
	            .setOAuthConsumerSecret("8QO7CenBz2oKrMqn6J20PjcqgyvMEAlwx5KcvkvpcAHoyTLHIQ")
	            .setOAuthAccessToken("231861677-DKJX2lNpGwIoDhl8ytt34WccOOSiWknypeqk4QJJ")
	            .setOAuthAccessTokenSecret("fcIyyqBaq2mGkPZQlZm7ppgIwoioOWJbASNN0Twl6JOYO");
	    cb.setJSONStoreEnabled(true);
	    
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
	        
        try {
        	BufferedWriter out = new BufferedWriter(new FileWriter("output.json"));
        	Query query = new Query().geoCode(new GeoLocation(-37.814251, 144.963169),100,"mi");
        	QueryResult result;
        	long lastID = 0;
        	
        	result = twitter.search(query);
    		List<Status> tweets = result.getTweets();
    		for (Status tweet : tweets) {
    			String json = DataObjectFactory.getRawJSON(tweet);
    			System.out.println(json);
    	        out.write(json+"\n");
    	        lastID = tweet.getId();
    		}
        	int queryLimit = 1;
        	do {
        		query.sinceId(lastID);
        		result = twitter.search(query);
        		tweets = result.getTweets();
        		for (Status tweet : tweets) {
        			String json = DataObjectFactory.getRawJSON(tweet);
        			System.out.println(json);
        	        out.write(json+"\n");
        	        lastID = tweet.getId();
        		}
        	} while ((query = result.nextQuery()) != null || ++queryLimit < 450);
        	out.close();
        	System.exit(0);
        } catch (TwitterException te) {
        	te.printStackTrace();
        	System.out.println("Failed to search tweets: " + te.getMessage());
        	System.exit(-1);
        } catch (Exception e){}
    }
}