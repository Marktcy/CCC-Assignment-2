import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
 
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;
 
public class Main {
 
	/** The actual Twitter stream. It's set up to collect raw JSON data */
	private TwitterStream twitterStream;
	Properties prop = new Properties();
	FileOutputStream fos;
 
	public Main() {
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
 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
 
	public void startTwitter() {
 
		try {
			fos = new FileOutputStream(new File("twitterstream.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		// Set up the stream's listener (defined above),
		twitterStream.addListener(listener);
		
		System.out.println("Starting down Twitter sample stream...");
 
		// Set up a filter to pull out industry-relevant tweets
		FilterQuery filtro = new FilterQuery();    
		double[][] bb= {{144.593742, -38.433859}, {145.512529, -37.511274}};
		filtro.locations(bb);
		twitterStream.filter(filtro);
 
	}
 
	public void stopTwitter() {
 
		System.out.println("Shutting down Twitter sample stream...");
		twitterStream.shutdown();
 
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	StatusListener listener = new StatusListener() {
 
		// The onStatus method is executed every time a new tweet comes in.
		public void onStatus(Status status) {
			// The EventBuilder is used to build an event using the headers and
			// the raw JSON of a tweet
			System.out.println(status.getUser().getScreenName() + ": " + status.getText());
 
			System.out.println("timestamp : "+ String.valueOf(status.getCreatedAt().getTime()));
			try {
				fos.write(DataObjectFactory.getRawJSON(status).getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
 
		}
 
		// This listener will ignore everything except for new tweets
		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
		public void onScrubGeo(long userId, long upToStatusId) {}
		public void onException(Exception ex) {}
		public void onStallWarning(StallWarning warning) {}
	};
 
	public static void main(String[] args) throws InterruptedException {
 
		Main twitter = new Main();
		twitter.startTwitter();
		Thread.sleep(20000);
		twitter.stopTwitter();
 
	}
 
}