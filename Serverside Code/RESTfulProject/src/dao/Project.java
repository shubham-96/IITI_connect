package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dto.User;


public class Project { 
	
	public ArrayList<User> GetUsers_det(Connection connection) 
	{
		ArrayList<User> users = new ArrayList<User>();
		
			//String uname = request.getParameter("uname");
			PreparedStatement ps;
			try {
				ps = connection.prepareStatement("SELECT * FROM User");
				ResultSet rs = ps.executeQuery();
				while(rs.next())
				{
					User user = new User();
					user.setUsername(rs.getString("username"));
					user.setReg_id(rs.getInt("reg_id"));
					user.setPassword(rs.getString("password"));
					users.add(user);
				}
				return users;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			//ps.setString(1,uname);
			
		
		
	}
	public static  String Adduser(Connection connection,String username,String first_name,String last_name,String password, byte[] pic,String mail){
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("SELECT MAX(reg_id) as max FROM User");
			ResultSet rs = ps.executeQuery();
			rs.next();
			int x= rs.getInt("max")+3;
			ps=null;
			ps = connection.prepareStatement("Insert into User values (?,?,?,?,?,?,?)");
			ps.setInt(1, x);
			ps.setString(2,first_name );
			ps.setString(3,last_name );
			ps.setString(4,password );
			ps.setString(5,username );
			ps.setBytes(6, pic);
			ps.setString(7, mail);
			 ps.execute();
			
			
			
			return "Success";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "in proj \n"+e.getMessage();
		}
		
	}
	public static  String login(Connection connection,String username,String password){
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("SELECT reg_id,password  FROM User where username= "+"\""+username+"\"");
			ResultSet rs = ps.executeQuery();
			rs.next();
			String pa = rs.getString("password");
			if(pa.equals(password))
				return rs.getString("reg_id");
			else
				return "Invalid/Wrong login credentials";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Invalid/Wrong login credentials";
		}
		
	}
	
	
	
}
