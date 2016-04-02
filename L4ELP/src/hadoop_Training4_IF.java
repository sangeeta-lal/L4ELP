

import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import  static org.eclipse.jdt.core.dom.ASTNode.CATCH_CLAUSE;



import java.io.*;
import java.lang.instrument.ClassDefinition;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.jdt.core.dom.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* ===========================@Author Sangeeta========
 *  * This file is used to create database that can be used by python training  module
 *  * This will fill the database with desired features
 *  * http://stackoverflow.com/questions/25871510/extracting-method-call-from-catch-blocks-java
 *  * http://grepcode.com/file/repository.grepcode.com/java/eclipse.org/3.6/org.eclipse.jdt/core/3.6.0/org/eclipse/jdt/core/dom/MethodInvocation.java#MethodInvocation.%3Cinit%3E%28org.eclipse.jdt.core.dom.AST%29
 * */

public class hadoop_Training4_IF
{

	//@Contextual fLAGS
	int is_method_have_param                   = 0;
	int method_param_count                     = 0;
	String method_param_as_string_original     = "";
	String method_param_as_string              = "";
	String  method_param_type  ="";
	String method_param_name = "";

	//@@ Flags between start of method and before start of IF Block
	int loc_till_if             = 0;
	int is_till_if_logged       = 0 ;
	int till_if_log_count       = 0;
	String till_if_log_levels   = "";
	String operators_till_if    = "";
	int operators_count_till_if = 0;
	String variables_till_if    = "";
	int variables_count_till_if = 0;
	String method_call_names_till_if  =  "";
	int method_call_count_till_if     =  0;
    int is_return_till_if=0; 
	int throw_throws_till_if = 0; 
	int if_in_till_if = 0;
	int if_count_in_till_if = 0 ;
	int is_assert_till_if =0;

	//@Note If flags
	int is_return_in_if=0;
	int throw_throws_if = 0;
	int is_assert_if = 0;
	int is_null_condition_if = 0;
	int is_instance_of_condition_if = 0;
	int is_if_logged   =  0;
    int if_log_count   =  0;
    String if_log_levels  =  "";

	
	
	List method_parameter ;
	String rawContent = "";
	String method_name = "";
	String method_content="";
    String class_name="";
	String package_name = "";
	String temp_file_path = "";
	
	
	//Set using the function of the file
	int id = 0;
	int log_count = 0;
	ArrayList<String> all_file_list= new ArrayList<String>();
	String log_levels_combined = "";
		
	///*
	String url = "jdbc:mysql://localhost:3306/";
	String driver = "com.mysql.jdbc.Driver";
	String db_name ="logging_level3";
	String userName = "root"; 
	String password = "1234";
	String table ="cloudstack_if_training3";	
    String listing_file_path = "F:\\Research\\Logging3\\result\\hd_java_files.txt";
   
    String non_logged_file_path = "F:\\Research\\Logging3\\result\\hd_non_log_if.txt";
	String logged_file_path = "F:\\Research\\Logging3\\result\\hd_log_if.txt";
	
	//String listing_file_path = "D:\\Research\\Logging3\\result\\temp_files.txt";
	//*/
    //@Note: create this file using create_file_listing.py
	/*
	String folder_path = "";
	String url = "jdbc:mysql://localhost:3307/";
	String driver = "com.mysql.jdbc.Driver";
	String db_name ="logging_level3";
	String userName = "sangeetal"; 
	String password = "sangeetal";
	String table ="cloudstack_if_training3";
    String listing_file_path = "E:\\Sangeeta\\Research\\Logging3\\result\\hd_java_files.txt"; 
   
    String non_logged_file_path = "E:\\Sangeeta\\Research\\Logging3\\result\\hd_non_log_if.txt";
	String logged_file_path = "E:\\Sangeeta\\Research\\Logging3\\result\\hd_log_if.txt";
	
     
   //*/
	 
    Connection conn=null;	
	java.sql.Statement stmt = null;
		
	public static void main(String[] args) 
	{				    
		hadoop_Training4_IF demo = new hadoop_Training4_IF();
		demo.conn = demo.initdb(demo.db_name);
		try {
			BufferedReader br =  new BufferedReader(new FileReader(demo.listing_file_path));
			String file_name =  br.readLine();
			while(br!=null)
			{
				System.out.println("Parsing File="+file_name);
				int len = (file_name.split("\\\\")).length;
				demo.package_name = file_name.split("\\\\")[len-2];
				demo.temp_file_path= file_name;//
				//demo.id = 0;
				demo.ast_prser(file_name);
				file_name =  br.readLine();
				//br=null;
			}
		} 
		catch (FileNotFoundException e) 
		{
		   System.out.println("Error.. Can ot open the listing file");
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
			
	}
	
public void ast_prser(String file_name)
	{
		try
		{
			
			rawContent = hadoop_Training4_IF.readFileToString(file_name);
			
		}catch(Exception e){
			System.out.println();
		}
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(rawContent.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		try{
	    cu.accept(
	    		
	    			new ASTVisitor() 
	    				{
	    	            	public boolean visit(ImportDeclaration id) 
	    	            	{
	    	                   Name imp = id.getName();
	    	                   //System.out.println("import =" + id.getName().getFullyQualifiedName());
	    	                   return false;
	    	                }

	    	               public boolean visit(VariableDeclarationFragment node) 
	    	                { 
	    	                  SimpleName name = node.getName();
	    	                  //System.out.println("var.declaration =" + (name.getFullyQualifiedName() + ":" + cu.getLineNumber(name.getStartPosition())));
	    	                  return false; // do not continue 
	    	                }	    	        
	    	        
	    	               public boolean visit(TypeDeclaration node)
	    	               { 
	    	            	   String name = node.getName().toString();
	    	            	   //System.out.println("   calss declaration =" + name);
	    	            	   class_name = name;
	    	            	   return true; // do not continue 
	    	               }

	    	               public boolean visit(MethodDeclaration method)
	    	               {
	    	            	    //id=0;
	    	        	      	method_name = method.getName().getFullyQualifiedName();
	    	        	      	method_parameter = method.parameters();
	    	        	       
	    	        	      	try
	    	        	      	{
	    	        	      	  Block methodBlock = method.getBody();
	    	        	      	  String myblock = methodBlock.toString();
	    	        	      	  method_content = methodBlock.toString();    	        	      	  
	    	        	          method_content = method_content.replaceAll("<\\?>","   ");	 // @@@ Note adding two spaces for reason because it was giving error  //
									                                                             //    in some cases if use  single space here " tr } } "             //   	        	      	  
	    	        	      	  method_content = method_content.trim();
	    	        	   
	    	        	      	  System.out.println("Myblock="+myblock);
	    	        	          methodVisitor(myblock);
	    	        	          //visitMethodDeclarion(myblock);
	    	        	      	}catch(java.lang.NullPointerException jnull)
	    	        	      	{
	    	        	      		System.out.println("Method Body in NULL");
	    	        	      		jnull.printStackTrace();	
		    	        	      	reset_if_flags();
		    	        	      //  insert("11", "","");
	    	        	      	}
	    	                   catch(Exception e)
	    	                   {
	    	                     e.printStackTrace();
	    	                     reset_if_flags();
	    	        	        //insert("12", "","");
   	        	      	       }
	    	            
	    	                    /*reset_parameters();
	    	        	        insert("13", "",""); 	        	      	
	    	        	      	System.out.println("Hello");*/
	    	        	      	return false;
	    	               }            
	               }
	    	);
		}catch(NullPointerException e) { e.printStackTrace();}
}

public void methodVisitor(String content) 
{
	ASTParser metparse = ASTParser.newParser(AST.JLS3);
    metparse.setSource(content.toCharArray());
    metparse.setKind(ASTParser.K_STATEMENTS);
    Block block = (Block) metparse.createAST(null);

    block.accept(new ASTVisitor() 
    {
          public boolean visit(VariableDeclarationFragment var) {
           // debug("met.var", var.getName().getFullyQualifiedName());
        	//System.out.println("dec");
            return true;
        }

        public boolean visit(SimpleName node) {
            //System.out.println(" Simple Node="+node.toString());
        	//System.out.println("Node");
            return true;
        }        
        public boolean visit(ForStatement myfor) {
           //System.out.println("myfor="+myfor.toString());
           //System.out.println("for");
            return true;
        }        
        public boolean visit(SwitchStatement myswitch) {
            //System.out.println("myswitch="+myswitch.toString());
             return true;
         }
        public boolean visit(DoStatement mydo) {
            //System.out.println("mydo="+mydo.toString());
             return true;
         }
        public boolean visit(WhileStatement mywhile) {
            //System.out.println("mywhile="+mywhile.toString());
             return true;
         }
         
        public boolean visit(IfStatement myif) 
        {
        	reset_if_flags();
        	
        	//object for method
        	method_name_and_count mnc_till_if =  new method_name_and_count();
        	operator_and_operator_count oaoc_till_if = new operator_and_operator_count();
        	log_level_interface tli_till_if = new log_level_interface();
        	
        	log_level_interface tli_if = new log_level_interface();
        	
        	String method_if_between_con = " " ;    	    
        	util3_met utm =  new util3_met();
        	
        	
        	//Contextualfeaturess                
        	is_method_have_param = utm.check_method_parameter (method_parameter);
        	method_param_as_string_original = method_parameter.toString();
        	method_param_as_string_original  = utm.replace_quotes_string(method_param_as_string_original);
        	method_param_as_string =  method_param_as_string_original;
        	method_param_as_string = utm.clean_method_params(method_param_as_string);
        	method_param_count = utm.get_param_count(method_parameter);    
        	
        	method_param_type= utm.get_method_param_type(method_parameter);
        	method_param_type = utm.clean_method_params(method_param_type);
        	
        	method_param_name= utm.get_method_param_name(method_parameter);
        	method_param_name = utm.clean_method_params(method_param_name);
        	
        	
        	//@ Note:if train content  = content between method and IF condition 
        	int if_pos = myif.getStartPosition();             
            method_if_between_con = method_content.substring(0, if_pos);  
            method_if_between_con  =  method_if_between_con.trim();
            method_if_between_con  =  utm.balance_closing_braces(method_if_between_con);
            
           //@Note: Feature Extraction Code
           loc_till_if = utm.get_loc(method_if_between_con);
             
           tli_till_if = utm.find_and_set_logging_level(method_if_between_con, tli_till_if);
           is_till_if_logged  = tli_till_if.logged;
           till_if_log_count =  tli_till_if.log_count;
           till_if_log_levels = tli_till_if.log_levels_combined;
         	 
         //	mnc_till_if =  utm.get_method_call_name(method_if_between_con, mnc_till_if);
         	String modified_con_for_method_call_ext  = utm.get_modified_con_for_method_cal_extraction(method_content , if_pos);
            mnc_till_if =  utm.get_method_call_name(modified_con_for_method_call_ext, mnc_till_if);
        	
          	method_call_names_till_if  = mnc_till_if.method_names;
          	method_call_count_till_if  = mnc_till_if.method_count;
              
          	oaoc_till_if  =  utm.get_operators_and_count(method_if_between_con , oaoc_till_if);
          	operators_till_if = oaoc_till_if.operator;
          	operators_count_till_if = oaoc_till_if.operator_count;
          	                
          	VariableVisitor_method_if_between_con(method_if_between_con);   
          	          	
          	is_return_till_if    = utm.check_return(method_if_between_con);
          	throw_throws_till_if  = utm.check_thorw_throws(method_if_between_con);
          	if_in_till_if         = utm.check_if(method_if_between_con);
          	if_count_in_till_if   = utm.get_if_count(method_if_between_con);
          	is_assert_till_if     = utm.check_assert(method_if_between_con);
        	
             // System.out.println("IF VALUE="+if_train_con+" ENDIF");
             
             /*
             int loc = mod_method_con.indexOf(mod_myif);
             
             if(loc==-1)
               {
            	 int if_loc = myif.getStartPosition();
            	 if_train_con = parent_content.substring(0,if_loc);            	 
        	     //System.out.println("loc ="+loc);
        	     System.out.println("expr"+myif.getExpression()+"if train="+if_train_con);
               }
             else
             {
            	if_train_con= mod_method_con.substring(0,loc); 
            	//System.out.println("loc ="+loc);
       	     	System.out.println("expr"+myif.getExpression()+"if train="+if_train_con);
              
             }*/
                         
            
          	//@Note : If Flags
           String if_expr = myif.getExpression().toString();
           String if_block = myif.toString();
           is_return_in_if    = utm.check_return(if_block);	
           throw_throws_if = utm.check_thorw_throws(if_block);
           is_assert_if= utm.check_assert(if_block);
           is_null_condition_if =   utm.check_null_condition(if_expr);
           is_instance_of_condition_if = utm.check_instanceOf_condition(if_expr);
           
           tli_if = utm.find_and_set_logging_level(if_block, tli_if);
           is_if_logged  = tli_if.logged;
           if_log_count =  tli_if.log_count;
           if_log_levels = tli_if.log_levels_combined;
              
            System.out.println("-------------------------------");
          	System.out.println("Class Name="+ class_name);
         	System.out.println("Method Name="+ method_name);
         	System.out.println("Package Name="+ package_name);
         	System.out.println("File Path="+ temp_file_path);
         	System.out.println("If Expression="+if_expr);
         	System.out.println("IF VALUE="+method_if_between_con+" ENDIF");
          	//find_and_set_logging_level(myif.toString());
            write_in_file(if_block, if_expr, method_if_between_con ,method_content,  log_count);
          	
            insert(if_block,if_expr.toString(),method_if_between_con,loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,
            		variables_count_till_if, method_call_names_till_if, method_call_count_till_if, is_return_till_if, throw_throws_till_if, if_in_till_if, 	if_count_in_till_if, 	is_assert_till_if ,
            		is_method_have_param, method_param_as_string_original, method_param_as_string, method_param_type, method_param_name, method_param_count, is_return_in_if, throw_throws_if, is_assert_if, 
            		is_null_condition_if, 	is_instance_of_condition_if, package_name, class_name, method_name, temp_file_path , is_if_logged ,  if_log_count ,  if_log_levels );
 	     
 	      	
         return true;
        }
                
        
        public boolean visit(TryStatement mytry) {
        	return true;
        }
       
        public boolean visit(CatchClause mycatch)
        {
          return true; 
        }

    }
    );
}



//******@Uses: This is a small visitor created to extract variable name and count****************// 
//*********************** from a given try block ********************************************//
//***********************************************************************************************//
public void VariableVisitor_method_if_between_con(String content) 
{
	ASTParser metparse = ASTParser.newParser(AST.JLS3);
  metparse.setSource(content.toCharArray());
  metparse.setKind(ASTParser.K_STATEMENTS);
  Block block = (Block) metparse.createAST(null);

     
  block.accept(new ASTVisitor() 
  {
        public boolean visit(VariableDeclarationFragment var) 
        {
         // debug("met.var", var.getName().getFullyQualifiedName());
      	variables_till_if            =   variables_till_if +" " + var.getName().getFullyQualifiedName();
      	variables_count_till_if      =   variables_count_till_if + 1;
      	                         
      	
      	System.out.println("Vriables in try:"+ variables_till_if);
          // interupt();
           return true;
          
      }

  }
  );
}

public void reset_if_flags()
{
	log_levels_combined="";
	log_count =0;
	//method_name = "";
	//method_content="";
	//class_name= "";	

	//@Note: Features between starting of method and try block
	operators_till_if    = "";
	operators_count_till_if = 0;
	variables_till_if = "";
	variables_count_till_if=0;
	loc_till_if=0;
	is_till_if_logged  = 0 ;
	till_if_log_count = 0;
	till_if_log_levels = "";
	method_call_names_till_if ="";
	method_call_count_till_if =0;
	is_return_till_if=0; 
	throw_throws_till_if = 0; 
	if_in_till_if = 0;
	if_count_in_till_if = 0 ;
	is_assert_till_if =0;
	
	//@Note: features of if block
	is_return_in_if=0;
	throw_throws_if = 0;
	is_assert_if = 0;
	is_null_condition_if = 0;
	is_instance_of_condition_if = 0;
	is_if_logged   =  0;
    if_log_count   =  0;
    if_log_levels  =  "";

}
	


public void insert(String if_block, String if_expr, String method_if_between_con, int loc_till_if,int is_till_if_logged, int till_if_log_count, String till_if_log_levels, String  operators_till_if,
		int operators_count_till_if,  String variables_till_if, int variables_count_till_if,  String method_call_names_till_if, int method_call_count_till_if, int is_return_till_if, 
		int throw_throws_till_if, int if_in_till_if, 	int if_count_in_till_if, 	int is_assert_till_if, int is_method_have_param, String method_param_as_string_original, String method_param_as_string, 
		String method_param_type, String method_param_name, int method_param_count,int  is_return_in_if, int throw_throws_if, int is_assert_if, int is_null_condition_if, int is_instance_of_condition_if, 
		String package_name, String class_name, String method_name,  String file_path,  int  is_if_logged , int  if_log_count , String if_log_levels )
 {
	
    id++;
  //  int logged= 0;
    
    /*if(log_count!=0)
    {
    	logged =1;
    }*/
    
    util3_met  utm =  new util3_met();	  
    method_if_between_con = utm.replace_quotes_string(method_if_between_con);
    if_block = utm.replace_quotes_string(if_block);
    
    method_if_between_con =  "I am not inserting too large for if";
    if_block = "too large not inserting if block";
    
    if_expr = utm.replace_quotes_string(if_expr);
    file_path =  file_path.replace("\\", "\\\\");
    
    String insert_str = " insert into " + table + " values("+ id+",'"+ if_block+"','"+ if_expr+"','"+ method_if_between_con+"',"+ loc_till_if+","+ is_till_if_logged+","+ till_if_log_count+",'"+
    		             till_if_log_levels+"','"+ operators_till_if+"',"+ operators_count_till_if+",'"+variables_till_if +"',"+variables_count_till_if+",'"+method_call_names_till_if+"',"+
    		             method_call_count_till_if+","+ is_return_till_if+ ","+ throw_throws_till_if +","+ if_in_till_if+","+ if_count_in_till_if+","+ is_assert_till_if+","+
    		             is_method_have_param +",'"+method_param_as_string_original+"','"+ method_param_as_string+"','"+ method_param_type+"','" +  method_param_name+"',"+ method_param_count+","
    		             + is_return_in_if+","+throw_throws_if+","+is_assert_if +","+is_null_condition_if+","+is_instance_of_condition_if+",'"+package_name+"','"+ class_name+"','"+ 
    		             method_name+"', '"+ file_path+"',"+ is_if_logged +","+      if_log_count +",'"+ if_log_levels+"'"+  " )";
    
    System.out.println("Insert str"+insert_str);
    try 
    	{
    		if(conn==null)
    			{
    				//System.out.println("I am null");
    			}
    		stmt =  conn.createStatement();
    		stmt.executeUpdate(insert_str);
    	} catch (SQLException e)
    	{ // TODO Auto-generated catch block
    		e.printStackTrace();
    	}
}

public void write_in_file(String if_block, String if_expr, String if_train_con,  String method_content, int log_count)
{
    BufferedWriter bw = null;
    int logged= 0;
    
    if(log_count!=0)
    {
    	logged =1;
    }
    
    if(logged==1)
    {
    	    	  
			   try
			   {
				 bw = new BufferedWriter(new FileWriter(logged_file_path,true));
				 
			  }  catch (IOException e)
			   {
				
				e.printStackTrace();
			 }		      	   
    }
    else
    {
    	 try
  	   {
			   bw = new BufferedWriter(new FileWriter(non_logged_file_path, true));
			   
		    
  	    } catch (IOException e) 
  	      { 
  	    	e.printStackTrace();
		      }
    }//else
   
    
    try 
    {
    	bw.write("File Path="+temp_file_path+"\n");
    	bw.write("Package Name ="+package_name+"\n");
    	bw.write("class name="+class_name+"\n");
    	bw.write("Method Name="+method_name+"\n");
    	bw.write("If Content="+ if_train_con+"\n");
    	//bw.write("All Catch Blocks="+ all_catch_as_string+"\n");
        //bw.write("method parameter = "+ method_parameter +"\n");
    	bw.write("method content="  +method_content+"\n");
        bw.write("--------------------\n");
		bw.close();
	} catch (IOException e) 
    {
	
		e.printStackTrace();
	}
       
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
	
public Connection initdb(String db_name)
{
	 try {
		      Class.forName(driver).newInstance();
		      conn = DriverManager.getConnection(url+db_name,userName,password);
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

}//main


/*
public void visit(final MethodCallExpr n, final Void arg)
{
    System.out.println(n);
    super.visit(n, arg);
}

@Override
public boolean visit(MethodInvocation node) {
    if (invocationsForMethods.get(activeMethod) == null) {
        invocationsForMethods.put(activeMethod, new ArrayList<MethodInvocation>());
    }
    invocationsForMethods.get(activeMethod).add(node);
    return super.visit(node);
}*/