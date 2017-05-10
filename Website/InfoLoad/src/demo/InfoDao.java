package demo;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;


public class InfoDao {
	
   public Info getAllInfo(){
	   Info info = null;
	   try{
		   CouchDbClient dbClientResult = new CouchDbClient("result", true, "http", "130.56.252.7", 5984, "admin", "password");
		   JsonObject json = dbClientResult.find(JsonObject.class,"8888"); 
		   info = new Info(json.get("json").toString());
		      
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return info;
   }
   
   public Info getMelPieInfo(){
	   Info info = null;
	   try{
		   CouchDbClient dbClientResult = new CouchDbClient("result", true, "http", "130.56.252.7", 5984, "admin", "password");
		   JsonObject json = dbClientResult.find(JsonObject.class,"melpieinfo"); 
		   info = new Info(json.get("json").toString());
		      
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return info;
   }
   
   public Info getSydPieInfo(){
	   Info info = null;
	   try{
		   CouchDbClient dbClientResult = new CouchDbClient("result", true, "http", "130.56.252.7", 5984, "admin", "password");
		   JsonObject json = dbClientResult.find(JsonObject.class,"sydpieinfo"); 
		   info = new Info(json.get("json").toString());
		      
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return info;
   }
   
   public Info getPerPieInfo(){
	   Info info = null;
	   try{
		   CouchDbClient dbClientResult = new CouchDbClient("result", true, "http", "130.56.252.7", 5984, "admin", "password");
		   JsonObject json = dbClientResult.find(JsonObject.class,"perpieinfo"); 
		   info = new Info(json.get("json").toString());
		      
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return info;
   }
   
   public Info getAdePieInfo(){
	   Info info = null;
	   try{
		   CouchDbClient dbClientResult = new CouchDbClient("result", true, "http", "130.56.252.7", 5984, "admin", "password");
		   JsonObject json = dbClientResult.find(JsonObject.class,"adepieinfo"); 
		   info = new Info(json.get("json").toString());
		      
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return info;
   }
   
   public Info getBriPieInfo(){
	   Info info = null;
	   try{
		   CouchDbClient dbClientResult = new CouchDbClient("result", true, "http", "130.56.252.7", 5984, "admin", "password");
		   JsonObject json = dbClientResult.find(JsonObject.class,"bripieinfo"); 
		   info = new Info(json.get("json").toString());
		      
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return info;
   }
   
   public Info getAuLineInfo(){
	   Info info = null;
	   try{
		   CouchDbClient dbClientResult = new CouchDbClient("result", true, "http", "130.56.252.7", 5984, "admin", "password");
		   JsonObject json = dbClientResult.find(JsonObject.class,"aulineinfo"); 
		   info = new Info(json.get("json").toString());
		      
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return info;
   }
   
   public Info getAuLineGDPInfo(){
	   Info info = null;
	   try{
		   CouchDbClient dbClientResult = new CouchDbClient("result", true, "http", "130.56.252.7", 5984, "admin", "password");
		   JsonObject json = dbClientResult.find(JsonObject.class,"aulinegdpinfo"); 
		   info = new Info(json.get("json").toString());
		      
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return info;
   }
   
   public Info getPlatformPieInfo(){
	   Info info = null;
	   try{
		   CouchDbClient dbClientResult = new CouchDbClient("result", true, "http", "130.56.252.7", 5984, "admin", "password");
		   JsonObject json = dbClientResult.find(JsonObject.class,"platformpieinfo"); 
		   info = new Info(json.get("json").toString());
		      
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return info;
   }
   
   public Info getPlatformSalesPieInfo(){
	   Info info = null;
	   try{
		   CouchDbClient dbClientResult = new CouchDbClient("result", true, "http", "130.56.252.7", 5984, "admin", "password");
		   JsonObject json = dbClientResult.find(JsonObject.class,"platformsalespieinfo"); 
		   info = new Info(json.get("json").toString());
		      
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return info;
   }
}