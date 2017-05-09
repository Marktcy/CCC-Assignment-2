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
import java.util.*;
import org.jclouds.ContextBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.cinder.v1.CinderApi;
import org.jclouds.openstack.cinder.v1.CinderApiMetadata;
import org.jclouds.openstack.cinder.v1.domain.Volume;
import org.jclouds.openstack.cinder.v1.features.VolumeApi;
import org.jclouds.openstack.cinder.v1.options.CreateVolumeOptions;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.NovaApiMetadata;
import org.jclouds.openstack.nova.v2_0.domain.*;
import org.jclouds.openstack.nova.v2_0.extensions.*;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import com.google.inject.Module;

//Using jclouds api with nova api and cinder api
public class NectarMain implements Closeable{
	private CinderApi cinderApi = null;
	private NovaApi novaApi = null;
    private Set<String> regions;

    //Menu section for nectar handling
    public static void main(String[] args) throws IOException {
    	NectarMain jcloudsNova = new NectarMain();

    	int response = -1;
    	Scanner scanner = new Scanner(System.in);
    	do{
    		System.out.print("\nNectar Handling Menu\n--------------------\n");
    		System.out.println("1. Create Key Pair.");
    		System.out.println("2. Create Instance.");
    		System.out.println("3. Create Volume.");
    		System.out.println("4. Attach Volume to instance.");
    		System.out.println("5. List instance.");
    		System.out.println("6. Auto creation.");
    		System.out.println("0. Exit.");
    		System.out.print("Please provide your options : ");
    		
    		response = scanner.nextInt();
    		String garbage = scanner.nextLine();
    		switch(response){
	    		case 1: 
	    			System.out.print("Please provide keypair name : ");
	    			String keypair = scanner.nextLine();
	    			System.out.print("Please provide public key directory : ");
	    			String pubFile = scanner.nextLine();
	    			jcloudsNova.createKeyPair(keypair, pubFile);break;
	    		case 2: 
	    			System.out.print("Please provide instance name : ");
	    			String crtInstanceName = scanner.nextLine();
	    			System.out.print("Please provide keypair name : ");
	    			String keyPair = scanner.nextLine();
	    			jcloudsNova.createInstance(crtInstanceName, keyPair);break;
	    		case 3: 
	    			System.out.print("Please provide volume size : ");
	    			int size = scanner.nextInt();
	    			garbage = scanner.nextLine();
	    			System.out.print("Please provide volume name : ");
	    			String crtVolumeName = scanner.nextLine();
	    			jcloudsNova.createVolume(size, crtVolumeName);break;
	    		case 4: 
	    			System.out.print("Please provide volumn name : ");
	    			String volumeName = scanner.nextLine();
	    			System.out.print("Please provide instance name : ");
	    			String instanceName = scanner.nextLine();
	    			jcloudsNova.attachVolume(volumeName, instanceName);
	    			break;
	    		case 5: jcloudsNova.listServers();break;
	    		case 6: jcloudsNova.autoCreate();break;
	    		case 0: jcloudsNova.close(); scanner.close(); break;
    		};
    		
    	}while(response != 0);
    }

    //Initializing and authentication
    public NectarMain() {
    	try{
	        Iterable<Module> modules = ImmutableSet.<Module>of(new SLF4JLoggingModule());
	
	        String provider = "openstack-nova";
	        String identity = "CCC2017-Team26:m.ting5@student.unimelb.edu.au";	//tenantname:username
	        String credential = "Yzg2YWZjM2RhZmJmM2Mx";							//token password
	
	        //Token authentication for nova api
	        novaApi = ContextBuilder.newBuilder(new NovaApiMetadata())
	                .endpoint("https://keystone.rc.nectar.org.au:5000/v2.0/")	//endpoint url
	                .credentials(identity, credential)
	                .modules(modules)
	                .buildApi(NovaApi.class);
	        regions = novaApi.getConfiguredRegions();
	        
	        //Token authentication for cinder api
	        cinderApi = ContextBuilder.newBuilder(new CinderApiMetadata())
	                .endpoint("https://keystone.rc.nectar.org.au:5000/v2.0/")	//endpoint url
	                .credentials(identity, credential)
	                .modules(modules)
	                .buildApi(CinderApi.class);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    //Keypair creation for ssh use
    private void createKeyPair(String name, String path) {
    	for (String region : regions) {
	        KeyPairApi keypairApi = this.novaApi.getKeyPairApi(region).get();
	        BufferedReader br = null;
	        try {
	            br = new BufferedReader(new FileReader(path));
	            StringBuilder sb = new StringBuilder();
	            String line;
	            while ((line = br.readLine()) != null) 
	                sb.append(line);
	            
	            line = sb.toString();
	            keypairApi.createWithPublicKey(name, line);
	        } catch (IOException e) {
	            System.out.println("ERROR:Given file path is not valid.");
	        } catch (Exception e){
	        	System.out.println("This key name have been use. Please try again");
	        }finally {
	        	System.out.println("Key pair successfully create.");
	            try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
    	}
    }
    
    //Instance creation based on insatance name and key pair name
    private void createInstance(String instanceName, String keypair){
    	try{
    		for (String region : regions) {
    	        ServerApi serverApi = novaApi.getServerApi(region);
    	        String imageId = "18bba5e4-d266-4209-9dde-2336465f0384";	//default Ubuntu 16.04
    	        String flavorId = "1";
    	        
    	        //Default setting for security group and availabity zone
    	        CreateServerOptions options = CreateServerOptions.Builder
    	                .keyPairName(keypair).securityGroupNames("default","ssh","TCP")
    	                .availabilityZone("NCI");
    	        ServerCreated ser = serverApi.create(instanceName, imageId, flavorId, options);
    	
    	        Server server = serverApi.get(ser.getId());
    	        while(server.getStatus().value().equals("ACTIVE") == false) {
    	            try {
    	                Thread.sleep(1000);
    	            }catch (InterruptedException ex) {}
    	            server = serverApi.get(ser.getId());
    	        }
        	}
    		System.out.println("Instance successfully create.");
    	}catch(Exception e){
    		System.out.println("Fail creation. Please check the instance size or instance name.");
    		System.out.println("Or if the keypair doesn't exist.");
    	}
    }
    
    //Create new volume to attach to the instance
    //Using value of system size and volume name
    private void createVolume(int size, String name) {
    	try{
    		for (String region : regions) {
	    		VolumeApi volumeApi = this.cinderApi.getVolumeApi(region);
	    		CreateVolumeOptions options = CreateVolumeOptions.Builder.name(name)
	    	    		.availabilityZone("melbourne-np").volumeType("melbourne");
	
	            Volume vol = volumeApi.create(size, options);
    		}
    		System.out.println("Volume successfully create.");
    	}catch(Exception e){
    		System.out.println("Fail creation. Please check the volume size or volume name.");
    	}
    }
    
    //Attach volume with parameter of volume name and instance name
    //Default partion name of "/dev/vdc"
    //Volume Attachment
    private void attachVolume(String volume, String server) {
    	try{
    		for (String region : regions) {
		        VolumeAttachmentApi volumeAttachmentApi = this.novaApi.getVolumeAttachmentApi(region).get();
		        volumeAttachmentApi.attachVolumeToServerAsDevice(
		                this.getVolumeId(volume), this.getServerId(server), "/dev/vdc");
		        String temp = (volumeAttachmentApi.getAttachmentForVolumeOnServer( this.getVolumeId(volume), this.getServerId(server)).getDevice());
		        System.out.println(volume + " attached to device : " + temp + "in" + server);
	    	}
	    	System.out.println("Volume successfully attached.");
    	}catch(Exception e){
    		System.out.println("Attached fail. Please check again.");
    	}
    }
    
    //Retrieve volume id based on volume name for volume attachment
    public String getVolumeId(String volumeName) {
    	for (String region : regions) {
	        VolumeApi volumeApi = this.cinderApi.getVolumeApi(region);
	        try {
	            Volume volumeObj = volumeApi.get(volumeName);
	            return volumeObj.getId();
	        } catch (NullPointerException e) {
	            Iterator<? extends Volume> volumeList = volumeApi.list().iterator();
	            while (volumeList.hasNext()) {
	                Volume v = volumeList.next();
	                if (v.getName().equalsIgnoreCase(volumeName))
	                    return v.getId();
	            }
	        }
    	}
        throw new NullPointerException("Volume not Found");
    }
    
    //Retrieve server id based on server name for volume attachment
    public String getServerId(String serverName) {
    	for (String region : regions) {
            ServerApi serverApi = novaApi.getServerApi(region);
            for (Server server : serverApi.listInDetail().concat()){
            	if(server.getName().equals(serverName))
            		return server.getId();
            }
        }
        throw new NullPointerException("Server not found");
    }
    
    //Listing all instance from nectar
    private void listServers() {
        for (String region : regions) {
            ServerApi serverApi = novaApi.getServerApi(region);
            System.out.println("Instances in " + region);

            for (Server server : serverApi.listInDetail().concat()){
            	System.out.println("   Name : " + server.getName() + " IP : " + server.getAccessIPv4());
                System.out.println("   Full Detail : " + server);
            }
        }
    }
    
    //One click auto creation for project purpose
    private void autoCreate(){
    	System.out.println("Creating Private key pair");
    	createKeyPair("PrivateKey", "cloud.key.pub");
    	
    	System.out.println("Creating Four instance");
    	createInstance("InstanceOne", "PrivateKey");
    	createInstance("InstanceTwo", "PrivateKey");
    	createInstance("InstanceThree", "PrivateKey");
    	createInstance("InstanceFour", "PrivateKey");
    	
    	System.out.println("Creating Four Volume");
		createVolume(60, "VolOne");
		createVolume(60, "VolTwo");
		createVolume(60, "VolThree");
		createVolume(60, "VolFour");
		
		System.out.println("Attached Four Volume to Four Instance");
		attachVolume("VolOne", "InstanceOne");
		attachVolume("VolTwo", "InstanceTwo");
		attachVolume("VolThree", "InstanceThree");
		attachVolume("VolFour", "InstanceFour");
		
		System.out.println("Finish creation.");
		listServers();
    }
    
    //Close the cloud handling section
    public void close() throws IOException {
        Closeables.close(novaApi, true);
    }
}