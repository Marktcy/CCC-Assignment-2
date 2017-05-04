/****************************************************
 * COMP90024 Cluster and Cloud Computing Project 2	*
 * File : NectarMain.java							*
 * Author : CCC2017 - Team26						*
 * City : Melbourne									*
 * Member : Shixun Liu, 766799						*
 * Member : Yuan Bing, 350274						*
 * Member : Renyi Hou, 764696						*
 * Member : Mark Chun Yong Ting, 805780				*
 * Member : Kaiqing Wang, 700275					*
 * Date : 30/4/2017									*
****************************************************/
import java.io.*;
import java.util.Properties;
import org.lightcouch.CouchDbClient;
import com.google.gson.*;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
 
public class Main {
	
	private TwitterStream twitterStream;
	Properties prop = new Properties();
	CouchDbClient dbClientMel, dbClientSyd, dbClientBri, dbClientPer, dbClientAde;
	
	//Main initialization for twitter and couchdb client
	public Main(String ip){
		try {
			prop.load(new FileInputStream("twitter.properties"));
 
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setOAuthConsumerKey(prop.getProperty("CONSUMER_KEY"));
			cb.setOAuthConsumerSecret(prop.getProperty("CONSUMER_SECRET"));
			cb.setOAuthAccessToken(prop.getProperty("ACCESS_TOKEN"));
			cb.setOAuthAccessTokenSecret(prop.getProperty("ACCESS_TOKEN_SECRET"));
			cb.setJSONStoreEnabled(true);
			cb.setIncludeEntitiesEnabled(true);
 
			twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
			
			dbClientMel = new CouchDbClient("twittermel", true, "http", ip, 5984, "admin", "password");
            dbClientSyd = new CouchDbClient("twittersyd", true, "http", ip, 5984, "admin", "password");
            dbClientBri = new CouchDbClient("twitterbri", true, "http", ip, 5984, "admin", "password");
            dbClientPer = new CouchDbClient("twitterper", true, "http", ip, 5984, "admin", "password");
            dbClientAde = new CouchDbClient("twitterade", true, "http", ip, 5984, "admin", "password");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	//When twitter API get tweet, it will call the on status function
	public void startTwitter() {
		twitterStream.addListener(listener);
		FilterQuery filtro = new FilterQuery();    
		double[][] aus= {{110.951034, -54.833766}, {159.287222, -9.187026}};
		filtro.locations(aus);
		twitterStream.filter(filtro);
	}
 
	public void stopTwitter() {
		dbClientMel.shutdown();
		dbClientSyd.shutdown();
		dbClientBri.shutdown();
		dbClientPer.shutdown();
		dbClientAde.shutdown();
		
		twitterStream.shutdown();
	}
 
	StatusListener listener = new StatusListener() {
 
		//Retrieve json data from tweets
		public void onStatus(Status status) {
			JsonParser parser = new JsonParser();
			JsonObject o = (JsonObject)parser.parse(TwitterObjectFactory.getRawJSON(status));
        	JsonObject tweetDetail = (JsonObject) parser.parse(TwitterObjectFactory.getRawJSON(status));
			JsonObject place = (JsonObject) tweetDetail.get("place");
			String placeName = place.get("full_name").getAsString();
			
			JsonObject object = new JsonObject();
			object.addProperty("_id", ""+status.getId());
			object.addProperty("rev", "3");
			object.add("json",o);
			
			//Compare and store only if is within five of our require location
			if(placeName.contains("Melbourne")){
				dbClientMel.save(object);	
			}else if(placeName.contains("Sydney")){
				dbClientSyd.save(object);
			}else if(placeName.contains("Brisbane")){
				dbClientBri.save(object);
			}else if(placeName.contains("Perth")){
				dbClientPer.save(object);
			}else if(placeName.contains("Adelaide")){
				dbClientAde.save(object);
			}
		}
		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
		public void onScrubGeo(long userId, long upToStatusId) {}
		public void onException(Exception ex) {}
		public void onStallWarning(StallWarning warning) {}
	};
 
	public static void main(String[] args) throws InterruptedException {
		Main twitter = new Main(args[1]);
		twitter.startTwitter();
	}
}