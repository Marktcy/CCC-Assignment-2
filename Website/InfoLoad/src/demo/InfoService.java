package demo;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.JSONP;

import javax.ws.rs.QueryParam;


@Path("/InfoService")
public class InfoService {

   InfoDao infoDao = new InfoDao();

   @GET
   @Path("/infos")
   @Produces(MediaType.APPLICATION_XML)
   public Info getInfo(){
      return infoDao.getAllInfo();
   }
   
   @GET
   @Path("/info")
   @Produces(MediaType.APPLICATION_JSON)
   public Info getJson() {
	   return infoDao.getAllInfo();
   }
   
   @GET      
   @Path("/getInfoJsonp")    
   @JSONP(queryParam="callback")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
   @Produces(MediaType.TEXT_PLAIN)  
   public String getInfoJsonp(@QueryParam("data") String data,@QueryParam("callback") String jsonpcallback) {      
     return jsonpcallback+"("+infoDao.getAllInfo().toString()+")";  
   }
   
   @GET      
   @Path("/melPieInfo")    
   @JSONP(queryParam="callback")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
   @Produces(MediaType.TEXT_PLAIN)  
   public String getMelPieInfo(@QueryParam("data") String data,@QueryParam("callback") String jsonpcallback) {      
     return jsonpcallback+"("+infoDao.getMelPieInfo().toString()+")";  
   }
   
   @GET      
   @Path("/sydPieInfo")    
   @JSONP(queryParam="callback")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
   @Produces(MediaType.TEXT_PLAIN)  
   public String getSydPieInfo(@QueryParam("data") String data,@QueryParam("callback") String jsonpcallback) {      
     return jsonpcallback+"("+infoDao.getSydPieInfo().toString()+")";  
   }
   
   @GET      
   @Path("/briPieInfo")    
   @JSONP(queryParam="callback")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
   @Produces(MediaType.TEXT_PLAIN)  
   public String getBriPieInfo(@QueryParam("data") String data,@QueryParam("callback") String jsonpcallback) {      
     return jsonpcallback+"("+infoDao.getBriPieInfo().toString()+")";  
   }
   
   @GET      
   @Path("/perPieInfo")    
   @JSONP(queryParam="callback")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
   @Produces(MediaType.TEXT_PLAIN)  
   public String getPerPieInfo(@QueryParam("data") String data,@QueryParam("callback") String jsonpcallback) {      
     return jsonpcallback+"("+infoDao.getPerPieInfo().toString()+")";  
   }
   
   @GET      
   @Path("/adePieInfo")    
   @JSONP(queryParam="callback")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
   @Produces(MediaType.TEXT_PLAIN)  
   public String getAdePieInfo(@QueryParam("data") String data,@QueryParam("callback") String jsonpcallback) {      
     return jsonpcallback+"("+infoDao.getAdePieInfo().toString()+")";  
   }
   
   @GET      
   @Path("/auLineInfo")    
   @JSONP(queryParam="callback")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
   @Produces(MediaType.TEXT_PLAIN)  
   public String getAuLineInfo(@QueryParam("data") String data,@QueryParam("callback") String jsonpcallback) {      
     return jsonpcallback+"("+infoDao.getAuLineInfo().toString()+")";  
   }
   
   @GET      
   @Path("/auLineGDPInfo")    
   @JSONP(queryParam="callback")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
   @Produces(MediaType.TEXT_PLAIN)  
   public String getAuLineGDPInfo(@QueryParam("data") String data,@QueryParam("callback") String jsonpcallback) {      
     return jsonpcallback+"("+infoDao.getAuLineGDPInfo().toString()+")";  
   }
   
   @GET      
   @Path("/platformPieInfo")    
   @JSONP(queryParam="callback")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
   @Produces(MediaType.TEXT_PLAIN)  
   public String getPlatformPieInfo(@QueryParam("data") String data,@QueryParam("callback") String jsonpcallback) {      
     return jsonpcallback+"("+infoDao.getPlatformPieInfo().toString()+")";  
   }
   
   @GET      
   @Path("/platformSalesPieInfo")    
   @JSONP(queryParam="callback")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
   @Produces(MediaType.TEXT_PLAIN)  
   public String getPlatformSalesPieInfo(@QueryParam("data") String data,@QueryParam("callback") String jsonpcallback) {      
     return jsonpcallback+"("+infoDao.getPlatformSalesPieInfo().toString()+")";  
   }
}