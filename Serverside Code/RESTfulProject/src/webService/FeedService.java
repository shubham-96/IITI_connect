package webService;

import java.sql.Connection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import dao.Database;
import dao.Project;
import model.ProjectManager;

@Path("/WebService")
public class FeedService {
	
	@GET
	@Path("/GetFeeds")
	public String signup(String username,String first_name,String last_name,final String mail){
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
						ProjectManager.email(mail, passwd);
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
	
	
	
	
	
	
		

}