package DB;

import java.sql.*;

public class DBConnection 
{
	public static Connection CreateConnection()
	{
		Connection conn = null;;
		
		String url = "jdbc:mysql://localhost:3306/toeicmyclass?useUnicode=true&amp&characterEncoding=utf8";
		String username = "root";
		String password = "root";
		
		try 
		{
			//load driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			//create connection
			conn = DriverManager.getConnection(url,username,password);
			
		} 
		catch (ClassNotFoundException e) 
		{

			e.printStackTrace();
		}
		catch (SQLException e) 
		{
			
			e.printStackTrace();
		}	
		return conn;
	}
}
