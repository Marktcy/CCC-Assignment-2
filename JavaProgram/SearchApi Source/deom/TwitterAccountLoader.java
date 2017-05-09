/****************************************************
 * COMP90024 Cluster and Cloud Computing Project 2	*
 * File : TwitterAccountLoader.java					*
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class TwitterAccountLoader {
	
	public static ArrayList<TwitterDev> loadTwitterAccount(String fileName){
		// read the twitter developer account info
		ArrayList<TwitterDev> twitterAccountList = new ArrayList<TwitterDev>();
		TwitterDev twitterAccount = null;
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
			String accountInfo = fileReader.readLine();
			
			while(accountInfo != null){
				String[] info = accountInfo.split("\t");
				
				String consumerKey = info[0];
				String consumerSecret = info[1];
				String accessToken = info[2];
				String accessTokenSecret = info[3];
				
				twitterAccount = new TwitterDev(consumerKey, consumerSecret, accessToken, accessTokenSecret);
				twitterAccountList.add(twitterAccount);
				
				accountInfo = fileReader.readLine(); 
			}
			
			fileReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return twitterAccountList;
		
	}

}
