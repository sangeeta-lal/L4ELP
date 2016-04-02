
import java.sql.Connection;
import java.sql.DriverManager;


public class db_conn 
{

	
	//private String dbName = "ANDROID";
	//private String TABLE = "bugdb2";
	private Connection conn = null;
    private String url = "jdbc:mysql://localhost:3306/";
     
    private String driver = "com.mysql.jdbc.Driver";
    private String userName = "root"; 
    private String password = "1234";
    
   
	
public Connection initdb(String db_name, String table)
{
	 try {
		      Class.forName(driver).newInstance();
		      conn = DriverManager.getConnection(url+db_name,userName,password);
		      
		 } catch (Exception e) {
		      e.printStackTrace();
		 }
		return conn;
}
	
public void closedb(Connection conn){ 
		 try {
		      conn.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	} 
	
}
