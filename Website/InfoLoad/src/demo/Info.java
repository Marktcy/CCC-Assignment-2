package demo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Info{
   private String info;
   
   public Info(){}
   
   public Info(String info){
      this.info = info;
   }

   public String getInfo() {
      return info;
   }

   public void setInfo(String info) {
      this.info = info;
   }
   
   public String toString(){
	return info;
	   
   }
   
}