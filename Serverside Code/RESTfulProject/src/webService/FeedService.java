package webService;

import java.sql.Connection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.database.connections.Database;
import com.database.connections.Project;
import com.datastructures.SOS;
import com.datastructures.User;
import com.datastructures.Warning;
import com.google.gson.Gson;

import model.ProjectManager;

@Path("/WebService")
public class FeedService {
	@GET
	@Path("/GetFeeds")
	public String signup(final String username,String first_name,String last_name,final String mail){
		System.out.println(username+" trying to sign up");
		 Database database= new Database();
		    Connection connection;
			try {
				connection = database.Get_Connection();
				if(!ProjectManager.checkusername(connection, username))
					return "Username Already Exists";
				if(!ProjectManager.checkmail(connection, mail))
					return "Mail ID Already Exists";
				final String passwd=ProjectManager.generatepasswd();
				String x = Project.Adduser(connection, username, first_name, last_name,passwd , null,mail);
				new Thread(new Runnable(){
					public void run(){
						ProjectManager.email(mail, passwd,"confirmation",username);
					}
				}).start();
				return x;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			}
		//	return "Successfully created. Check your mail for password";	
		    
	}
	@GET
	@Path("/GetFeeds")
	public String login(String username,String password){
		 Database database= new Database();
		    Connection connection;
			try {
				connection = database.Get_Connection();
				return  ""+Project.login(connection, username, password);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			}	
		    
	}
	
	
	@GET
	@Path("/GetFeeds")
	public String forgotpassword(String entity,String value){
		 Database database= new Database();
		    Connection connection;
			try {
				System.out.println("forgot password by "+value);
				connection = database.Get_Connection();
				return  ""+Project.forgotoasswd(connection,entity, value);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			}	
		    
	}
	
	@GET
	@Path("/GetFeeds")
	public String changepassword(int Reg_id,String oldpass,String newpass){
		 Database database= new Database();
		    Connection connection;
			try {
				connection = database.Get_Connection();
				return  ""+Project.changepasswd(connection,Reg_id, oldpass,newpass);
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
	}
	

	@GET
	@Path("/GetFeeds")
	public Boolean changedp(int Reg_id,String	 x){
		System.out.println("change dp called");
		 Database database= new Database();
		    Connection connection;
			try {
				
				connection = database.Get_Connection();
				//return false;
				return  Project.changedp(connection,Reg_id,x );
			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
	}
	
	@GET
	@Path("/GetFeeds")
	public String getdp(int Reg_id){
		System.out.println("get dp called");
		 Database database= new Database();
		    Connection connection;
			try {
				
				connection = database.Get_Connection();
				//return false;
				return  Project.getdp(connection,Reg_id );
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	
	@GET
	@Path("/GetFeeds")
	public String getuser(int Reg_id){
		System.out.println("get User called");
		 Database database= new Database();
		    Connection connection;
			try {
				
				connection = database.Get_Connection();
				//return false;
				User u =   Project.getuserobj(connection,Reg_id );
				String json = new Gson().toJson(u);
				return json;
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			return "";
	}
	
	@GET
	@Path("/GetFeeds")
	public String setWarning(int Reg_id,String Message,String type){
		
		
		
		Database database= new Database();
	    Connection connection;
		try {
			
			connection = database.Get_Connection();
			System.out.println("Warning set");
			Warning.setWarning(Project.getusername(connection, Reg_id),Message, type); 
			return "Successfuly set";
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return "";
	}
	
	@GET
	@Path("/GetFeeds")
	public String getWarning(){
		return Warning.getWarning();
	}
	
	@GET
	@Path("/GetFeeds")
	public String isWarnignGenerated(){
		return Warning.isWarning()?"yes":"no";
	}
	
	@GET
	@Path("/GetFeeds")
	public String changeStatus(int reg_id,String status){
		
		System.out.println("get User called");
		 Database database= new Database();
		    Connection connection;
			try {
				
				connection = database.Get_Connection();
				return Project.changeStatus(connection,reg_id,status);
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
	}
	
	@GET
	@Path("/GetFeeds")
	public void clearWarning(){
		
		Warning.removeWarning();
	}
	
	
	
	
	
	
	@GET
	@Path("/GetFeeds")
	public String setSOS(int Reg_id,String Message,String type){
		
		
		
		Database database= new Database();
	    Connection connection;
		try {
			
			connection = database.Get_Connection();
			System.out.println("Warning set");
			SOS.setSOS(Project.getusername(connection, Reg_id),Message, type); 
			return "Successfuly set";
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return "";
	}
	
	@GET
	@Path("/GetFeeds")
	public String getSOS(){
		return SOS.getSOS();
	}
	
	@GET
	@Path("/GetFeeds")
	public String isSOSGenerated(){
		return SOS.isSOS()?"yes":"no";
	}
	
	
	@GET
	@Path("/GetFeeds")
	public void clearSOS(){
		
		SOS.removeSOS();
	}
	@GET
	@Path("/GetFeeds")
	public boolean wasMacSet(String mac){
		Database database= new Database();
	    Connection connection;
		try {
			
			connection = database.Get_Connection();
			return Project.wasMacset(connection,mac);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}
	@GET
	@Path("/GetFeeds")
	public boolean setAddress(String address,String mac,int reg_id){
		Database database= new Database();
	    Connection connection;
		try {
			
			connection = database.Get_Connection();
			return Project.setAddress(connection, reg_id, mac, address);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		System.out.println("Error in set Address");
		return true;
	}
	@GET
	@Path("/GetFeeds")
	public String getAddress(String mac){
		Database database= new Database();
	    Connection connection;
		try {
			
			connection = database.Get_Connection();
			return Project.getAddress(connection, mac);
	
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		System.out.println("Error in get Address");
		return "fail";
	}
	

}