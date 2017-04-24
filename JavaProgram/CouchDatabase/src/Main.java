import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException{
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("twitter.properties"));
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(prop.getProperty("CONSUMER_KEY"));
		cb.setOAuthConsumerSecret(prop.getProperty("CONSUMER_SECRET"));
		cb.setOAuthAccessToken(prop.getProperty("ACCESS_TOKEN"));
		cb.setOAuthAccessTokenSecret(prop.getProperty("ACCESS_TOKEN_SECRET"));
		cb.setJSONStoreEnabled(true);
		cb.setIncludeEntitiesEnabled(true);
	    
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
	        
        try {
        	Query query = new Query().geoCode(new GeoLocation(-37.814251, 144.963169),100,"mi");
        	QueryResult result;
        	
        	CouchDbClient dbClient = new CouchDbClient();
    		
        	result = twitter.search(query);
    		List<Status> tweets = result.getTweets();
    		for (Status tweet : tweets) {
    			String jsonStr = TwitterObjectFactory.getRawJSON(tweet);
    			JsonParser parser = new JsonParser();
    			JsonObject o = (JsonObject)parser.parse(jsonStr);
    			
    			JsonObject object = new JsonObject ();
    			object.addProperty("_id", ""+tweet.getId());
    			object.addProperty("rev", "3");
    			object.add("json",o);
    			try
    			{
    				dbClient.save(object);
    			}
    			catch(org.lightcouch.DocumentConflictException e)
    			{
    				//if we insert something that already exists
    				//we get Exception in thread "main" org.lightcouch.DocumentConflictException: << Status: 409 (Conflict)
    			}
    		}

    		dbClient.shutdown();
        	System.exit(0);
        } catch (TwitterException te) {
        	te.printStackTrace();
        	System.out.println("Failed to search tweets: " + te.getMessage());
        	System.exit(-1);
        } catch (Exception e){}
	}
}
