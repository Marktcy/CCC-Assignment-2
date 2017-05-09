/****************************************************
 * COMP90024 Cluster and Cloud Computing Project 2	*
 * File : connectCouchDB.java						*
 * Author : CCC2017 - Team26						*
 * City : Melbourne									*
 * Member : Shixun Liu, 766799						*
 * Member : Yuan Bing, 350274						*
 * Member : Renyi Hou, 764696						*
 * Member : Mark Chun Yong Ting, 805780				*
 * Member : Kaiqing Wang, 700275					*
 * Date : 30/4/2017									*
****************************************************/

package Cloud_Computing;

import org.lightcouch.CouchDbClient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

import org.lightcouch.CouchDbClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.PrintStream; 
import java.util.Properties; 
import java.util.Set; 
 
import javax.annotation.PostConstruct; 

// connect couchdb and fetch json data
public class ConnectCouchDB 
{
	CouchDbClient dbClientADE, dbClientMEL, dbClientSYD, dbClientBRI, dbClientPER, dbClientTEST;
	
	// overload the class
	public ConnectCouchDB()
	{
		// connect the couchdb database
		dbClientMEL = new CouchDbClient("twittermel", true, "http", "115.146.92.250", 5984, "admin", "password"); 
	}

	public static void main(String[] args) throws InterruptedException, IOException
	{
		ConnectCouchDB tw = new ConnectCouchDB();
		tw.run();
		System.out.println("Access Completed");
	}
	
	public void run() throws IOException
	{
		BufferedWriter bufferW;
		File file = new File(theOutputFileLocation);
		
		if(!file.exists())
		{
			file.createNewFile();
		}
		  
		List<JsonObject> allDocs_Test = dbClientMEL.view("_all_docs").query(JsonObject.class);
		bufferW = new BufferedWriter(new FileWriter(file));
		
		for (int i = 0; i < allDocs_Test.size(); i++){
			try{
				JsonObject myData = allDocs_Test.get(i);
				Gson gson = new Gson();
				JsonElement element = gson.fromJson(myData.toString(), JsonElement.class);
				String result = gson.toJson(element);
				
				//find the json data id
				JsonParser parser = new JsonParser();
				JsonObject idDetail = (JsonObject) parser.parse(result);
				JsonPrimitive inner = (JsonPrimitive) idDetail.get("id");
				
				if (inner == null)
					continue;
				
				// this is the id
				String newInner = null;
				if(inner.toString().contains("\\"))
					newInner = inner.toString().substring(2, inner.toString().length() - 3) + "\"";
				else
					newInner = inner.toString().substring(1, inner.toString().length() - 1);
				
				// find the exact json data through id(newInner)
				JsonObject json = dbClientMEL.find(JsonObject.class, newInner);
				JsonObject tweetDetail = (JsonObject) parser.parse(json.toString());
				JsonObject jsonDetail = (JsonObject) tweetDetail.get("json");
					
				// find the resource like iPhone, Android or other Application
				JsonPrimitive jsonSourceDetail = (JsonPrimitive)jsonDetail.get("source");
				if(jsonSourceDetail == null)
						continue;
				String jsonSourceString = jsonSourceDetail.toString();
				String jsonSourceReal = retrieveSourceFromTweet(jsonSourceString);

				// add the coordinates
				JsonObject coordinates = (JsonObject) jsonDetail.get("coordinates");
				if(coordinates == null)
					continue;
				JsonArray coor = (JsonArray) coordinates.get("coordinates");
					
				// find the createtime
				JsonPrimitive jsonCreateTime = (JsonPrimitive)jsonDetail.get("created_at");
				if(jsonCreateTime == null)
					continue;
				String jsonCreateTimeString = jsonCreateTime.toString();
					
				// add the Json text 
				JsonPrimitive jsonTweet = (JsonPrimitive)jsonDetail.get("text");
				if(jsonTweet == null)
					continue;
				String jsonTweetString = jsonTweet.toString();
					
				bufferW.write(newInner + ",,," + jsonSourceReal + ",,," + jsonCreateTimeString + ",,,"  + coor.get(0) + ",,," + coor.get(1) + ",,," + jsonTweetString + "\n");
				bufferW.flush();
			}
			catch (Exception e){}
		}
		bufferW.close();
	}
	
	// this function is used to filter the source in Tweet
	public String retrieveSourceFromTweet(String source)
	{
		String pattern = ">.+</a>";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(source);
		if (m.find()){	
			if (m.group(0).contains(" ")){
				String[] sp = m.group(0).split("\\s+");
				sp[sp.length - 1] = sp[sp.length - 1].replace("</a>", "");
				sp[sp.length - 1] = sp[sp.length - 1].replace(">", "");
				return sp[sp.length - 1];
			}
			else{
				String sourceString = m.group(0);
				sourceString = sourceString.replace("</a>", "");
				sourceString = sourceString.replace(">", "");
				return sourceString;
			}
		}
		return null;
	}
}
