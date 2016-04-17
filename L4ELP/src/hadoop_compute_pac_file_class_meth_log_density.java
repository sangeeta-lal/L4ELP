

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;


/* @ Author: Sangeeta
 * @Uses:This file will be used to compute the logging density of file/packages/class/methods to make graphs
*/
public class hadoop_compute_pac_file_class_meth_log_density 

{
	String rawContent = "";
	String log_levels_combined = "";
	
	//int file_LOC = 0;
	//int file_SLOC  = 0 ;
	
	///*
	 String url = "jdbc:mysql://localhost:3306/";
	 String driver = "com.mysql.jdbc.Driver";
	 String db_name ="logging4_elp";
	 String userName = "root"; 
	 String password = "1234";
	   
	//@Note: create this file using create_file_listing.py
	 String listing_file_path = "F:\\Research\\L4ELP\\result\\hd_java_files.txt";
	//String listing_file_path = "F:\\Research\\L4ELP\\result\\hd-test.txt";

	 String logged_file_path = "F:\\Research\\L4ELP\\result\\hd_log_all.txt";
	 String insert_table = "hd_file_logging_density";
	//*/
    
	/*
	 String url = "jdbc:mysql://localhost:3307/";
	 String driver = "com.mysql.jdbc.Driver";
	 String db_name ="logging4_elp";
	 String userName = "sangeetal"; 
	 String password = "sangeetal";
	
	//@Note: create this file using create_file_listing.py
	 String listing_file_path = "E:\\Sangeeta\\Research\\L4ELP\\result\\hd_java_files.txt";  
	 String logged_file_path = "E:\\Sangeeta\\Research\\L4ELP\\result\\hd_log_all.txt";
	  String insert_table = "hd_file_logging_density";
   //*/
	 
	 Connection conn=null;	
     java.sql.Statement stmt = null;
     
     public static void main(String[] args) 
 	{
        hadoop_compute_pac_file_class_meth_log_density  demo = new hadoop_compute_pac_file_class_meth_log_density();
 		demo.conn = demo.initdb(demo.db_name);

 		if(demo.conn==null)
 		{
 			System.out.println(" Databasse connection is null");
 			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 			try 
 			 {
 			  br.readLine();
 			 }catch(Exception e)
 			{}
 		}
 	    
 		//create new file for every run
 		try {
 		       BufferedWriter bw = new BufferedWriter(new FileWriter(demo.logged_file_path));
 		       bw.close();
 		      
 		       
 		} catch (IOException e1) 
 		  {
 		
 			e1.printStackTrace();
 		  }
               
 		
 		try {
 			BufferedReader br =  new BufferedReader(new FileReader(demo.listing_file_path));
 			String file_name =  br.readLine();
 			int file_count=0;
 			while(br!=null)
 			{ 
 				file_count++;
 				System.out.println("Parsing File="+file_name);
 				int len = (file_name.split("\\\\")).length;
 				//demo.complete_package_path = file_name.split("\\\\")[len-2];
 				//demo.temp_file_path= file_name.replace("\\", "\\\\");
 			  
 				demo.file_log_lines(file_name, file_count);
 				file_name =  br.readLine();
 				
 			}
 		} 
 		catch (FileNotFoundException e) 
 		{
 		   System.out.println("Error.. Can not open the listing file");
 			e.printStackTrace();
 		}
 		catch(IOException e)
 		{
 			e.printStackTrace();
 		}
 			
 	}
 	
 public void file_log_lines(String file_name, int file_count)
 	{
	
	 util4_met u3m= new util4_met(); 
	 log_level_interface l = new log_level_interface();
	 Remove_Comments  rc =  new Remove_Comments();
	 
 		try
 		{		
 			int file_final_sloc = 0;
  			String file_content_as_string = readFileToString(file_name);	
 			String file_content_without_comment =  rc.remove_comments(file_content_as_string);	
 			l=  u3m.find_and_set_logging_level(file_content_without_comment, l);		
 			file_final_sloc= u3m.find_final_file_SLOC(file_content_without_comment);
 			file_name=  file_name.replace("\\", "\\\\");
 	     	//System.out.println(" file name="+ file_name+ " loc = "+ file_final_sloc+ " logged="+l.logged+ "count="+ l.log_count+ "new loc"+ file_final_sloc);		
 			insert_to_db(file_count, file_name, file_final_sloc, l.logged, l.log_count, l.log_levels_combined);
 		}catch(Exception e)
 		{
 			e.printStackTrace();
 		}
 	
 	}
	 

private void insert_to_db(int file_count, String file_name, int file_final_sloc, int logged, int log_count,  String log_levels)
{
	String insert_str = "insert into "+ insert_table + " values("+file_count+", '"+ file_name+"',"+ file_final_sloc +","+ logged+","+ log_count+",'"+log_levels+"'" +")";
	
	Statement stmt =null;
	try 
	{
		stmt = conn.createStatement();
		stmt.executeUpdate(insert_str);
	} catch (SQLException e) 
	{
		e.printStackTrace();
	}
	
}

public Connection initdb(String db_name)
{
	 try {
		      Class.forName(driver).newInstance();
		      conn = DriverManager.getConnection(url+db_name,userName,password);
		      //System.out.println(" dbname="+ db_name+ "user name"+ userName+ " password="+ password);
		      if(conn==null)
		      {
		    	  System.out.println("Hi I am null :( :(");
		      }
		      
		 } catch (Exception e) 
		 {
		      e.printStackTrace();
		 }
		return conn;
}


public static String readFileToString(String filePath) throws IOException
	{
	    StringBuilder fileData = new StringBuilder(1000);
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));

	    char[] buf = new char[10];
	    int numRead = 0;
	    while ((numRead = reader.read(buf)) != -1) {
	        //          System.out.println(numRead);
	        String readData = String.valueOf(buf, 0, numRead);
	        fileData.append(readData);
	        buf = new char[1024];
	    }
	    reader.close();
	    return  fileData.toString();    
 }
  

}
