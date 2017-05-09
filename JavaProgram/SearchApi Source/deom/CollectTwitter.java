/****************************************************
 * COMP90024 Cluster and Cloud Computing Project 2	*
 * File : CollectTwitter.java						*
 * Author : CCC2017 - Team26						*
 * City : Melbourne									*
 * Member : Shixun Liu, 766799						*
 * Member : Yuan Bing, 350274						*
 * Member : Renyi Hou, 764696						*
 * Member : Mark Chun Yong Ting, 805780				*
 * Member : Kaiqing Wang, 700275					*
 * Date : 30/4/2017									*
****************************************************/
package deom;

import java.util.*;
import java.util.concurrent.TimeUnit;
import org.lightcouch.CouchDbClient;
import com.google.gson.*;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class CollectTwitter {

	Twitter twitter;
	Query query;
	QueryResult result;
	ArrayList<TwitterDev> twitterAccountList = null;
	CouchDbClient dbClient = null;
	JsonParser parser = new JsonParser();

	public static void main(String[] args) {
		if (args.length != 1) {
            System.err.println("Usage: required location name : (melbourne/sydney/adelaide/perth/brisbane)");
            System.exit(-1);
        }
		
		CollectTwitter ct = new CollectTwitter(args[0]);
		ct.run();
	}
	
	public CollectTwitter(String area){
		query = new Query();
		
		if(area.equalsIgnoreCase("melbourne")){
			twitterAccountList = TwitterAccountLoader.loadTwitterAccount("twitterDevMel.txt");
			dbClient = new CouchDbClient("twittermel", true, "http", "127.0.0.1", 5984, "admin", "password");
			query.setGeoCode(new GeoLocation(-37.814251, 144.963169),20.0, Query.KILOMETERS);
		}else if(area.equalsIgnoreCase("sydney")){
			twitterAccountList = TwitterAccountLoader.loadTwitterAccount("twitterDevSyd.txt");
			dbClient = new CouchDbClient("twittersyd", true, "http", "127.0.0.1", 5984, "admin", "password");
			query.setGeoCode(new GeoLocation(-33.865143, 151.209900),20.0, Query.KILOMETERS);
		}else if(area.equalsIgnoreCase("adelaide")){
			twitterAccountList = TwitterAccountLoader.loadTwitterAccount("twitterDevAde.txt");
			dbClient = new CouchDbClient("twitterade", true, "http", "127.0.0.1", 5984, "admin", "password");
			query.setGeoCode(new GeoLocation(-34.92866, 138.59863),20.0, Query.KILOMETERS);
		}else if(area.equalsIgnoreCase("brisbane")){
			twitterAccountList = TwitterAccountLoader.loadTwitterAccount("twitterDevBri.txt");
			dbClient = new CouchDbClient("twitterbri", true, "http", "127.0.0.1", 5984, "admin", "password");
			query.setGeoCode(new GeoLocation(-27.470125, 153.021072),20.0, Query.KILOMETERS);
		}else if(area.equalsIgnoreCase("perth")){
			twitterAccountList = TwitterAccountLoader.loadTwitterAccount("twitterDevPer.txt");
			dbClient = new CouchDbClient("twitterper", true, "http", "127.0.0.1", 5984, "admin", "password");
			query.setGeoCode(new GeoLocation(-31.953512, 115.857048),20.0, Query.KILOMETERS);
		}else{
			System.err.println("Usage: required location name (melbourne/sydney/adelaide/perth/brisbane)");
            System.exit(-1);
		}
	}
	
	public void run(){
		// read the twitter developer account info
		int nextAcc = 0;
		configureTwitterAcc(nextAcc++);
		
		long lastID = 0;
		do{
			try{
	    		int queryLimit = 0;
	        	do {
	        		query.setCount(100);
	        		query.sinceId(lastID);
	        		result = twitter.search(query);
	        		List<Status> tweets = result.getTweets();
	        		lastID = tweets.get(0).getId();
	        		for (Status tweet : tweets) {
	        			JsonObject object = new JsonObject ();
	        			object.addProperty("_id", Long.toString(tweet.getId()));
	        			object.addProperty("rev", "3");
	        			object.add("json",(JsonObject)parser.parse(TwitterObjectFactory.getRawJSON(tweet)));
	        			try{
	        				dbClient.save(object);
	        			}
	        			catch(org.lightcouch.DocumentConflictException e){}
	        		}
	        	} while ((query = result.nextQuery()) != null || ++queryLimit < 449);
	        	dbClient.shutdown();
				
			}catch(TwitterException te){
				try {
					TimeUnit.MINUTES.sleep(15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(nextAcc > (twitterAccountList.size() - 1))
					nextAcc = 0;
				
				configureTwitterAcc(nextAcc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}while(true);
	}
	
	public void configureTwitterAcc(int acc){
		TwitterDev twitterAccount = twitterAccountList.get(acc);
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(twitterAccount.getConsumerKey())
        	.setOAuthConsumerSecret(twitterAccount.getConsumerSecret())
        	.setOAuthAccessToken(twitterAccount.getAccessToken())
        	.setOAuthAccessTokenSecret(twitterAccount.getAccessTokenSecret());
		cb.setJSONStoreEnabled(true);
		twitter = new TwitterFactory(cb.build()).getInstance();
	}
}
