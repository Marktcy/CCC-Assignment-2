/****************************************************
 * COMP90024 Cluster and Cloud Computing Project 2	*
 * File : TwitterDev.java							*
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

public class TwitterDev {
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	
	public TwitterDev(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
	}
	
	public String getConsumerKey() {
		return consumerKey;
	}
	
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	
	public String getConsumerSecret() {
		return consumerSecret;
	}
	
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}
	
	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

}
