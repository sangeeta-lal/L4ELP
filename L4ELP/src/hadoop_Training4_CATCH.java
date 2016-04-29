

import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;


//import  static org.eclipse.jdt.core.dom.ASTNode.CATCH_CLAUSE;
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
 * 1. This file is the modification of previous training files as it combines all the string attributes together.
 *   
 *  */

public class hadoop_Training4_CATCH
{

	
	int method_count = 0 ;
	List  method_parameter ;// = Arrays.asList("sup1", "sup2", "sup3");
	String rawContent = "";
	String method_name = "";
	String method_content="";
	String class_name="";
	String package_name = "";
	String temp_file_path = "";
	
	//Set using the function of the file
	int try_id = 0;
	int catch_id = 0;
	
	int log_count = 0;
	
	//int logged_count=0;
	//int non_logged_count=0;
	
	//@Contextual fLAGS
	int is_method_have_param = 0;
	int method_param_count = 0;
	String method_param_as_string_original = "";
	String method_param_as_string = "";
	String method_param_type  = "";
	String method_param_name= "";

	
	//@@ TRY FLAGS
	String try_con="";
	int try_loc = 0;
	int is_try_logged = 0;
	int try_log_count = 0;
	String try_log_levels = "";
	int is_return_in_try=0;
	int is_thread_sleep_try = 0;
	int throw_throws_try = 0;
	int if_in_try=0;
	int if_count_in_try = 0;
	int is_assert_try = 0;
	String method_call_names_try = "";
	int method_call_count_try  = 0;
	String operators_in_try = "";
	int operators_count_try = 0;
	String variables_in_try = "";
	int variables_count_try = 0;
	
	//@@ Flags between start of method and before start of try block
	String method_call_names_till_try = "";
	int method_call_count_till_try  = 0;
	String operators_till_try = "";
	int operators_count_till_try = 0;
	String variables_till_try = "";
	int variables_count_till_try=0;
	int loc_till_try = 0 ;
	int is_till_try_logged = 0 ;
	int till_try_log_count = 0;
	String till_try_log_levels = "";
	int is_return_till_try=0; 
	int throw_throws_till_try = 0; 
	int if_in_till_try = 0;
	int if_count_in_till_try = 0 ;
	int is_assert_till_try =0;
	
	//@@ CATCH FLAGS
	int is_catch_logged = 0;
	int catch_log_count=0;
	String catch_log_levels = "";
	int have_previous_catches=0;
	int previous_catches_logged=0;
	int previous_catches_log_count=0;
	int catch_depth = 0;
	int is_return_in_catch=0;
	int is_catch_object_ignore=0;
	int is_interrupted_exception=0;
	int is_throwable_exception = 0;
	int throw_throws_catch = 0;
	int is_assert_catch = 0 ;

	
	ArrayList<String> all_file_list= new ArrayList<String>();
	String log_levels_combined = "";
	///*
	 String url = "jdbc:mysql://localhost:3306/";
	 String driver = "com.mysql.jdbc.Driver";
	 String db_name ="logging4_elp";
	 String userName = "root"; 
	 String password = "1234";
	   
	//@Note: create this file using create_file_listing.py
	 String listing_file_path = "F:\\Research\\L4ELP\\result\\hd_java_files.txt";
	 String non_logged_file_path = "F:\\Research\\L4ELP\\result\\hd_non_log_catch.txt";
	 String logged_file_path = "F:\\Research\\L4ELP\\result\\hd_log_catch.txt";
	 String table = "hd_catch_training4";
	//*/
    
	/*
	 String url = "jdbc:mysql://localhost:3307/";
	 String driver = "com.mysql.jdbc.Driver";
	 String db_name ="logging4_elp";
	 String userName = "sangeetal"; 
	 String password = "sangeetal";
	
	//@Note: create this file using create_file_listing.py
	 String listing_file_path = "E:\\Sangeeta\\Research\\L4ELP\\result\\hd_java_files.txt";  
	 String non_logged_file_path = "E:\\Sangeeta\\Research\\L4ELP\\result\\hd_non_log_catch.txt";
	 String logged_file_path = "E:\\Sangeeta\\Research\\L4ELP\\result\\hd_log_catch.txt";
	  String table = "hd_catch_training4";
   //*/
	 
	 Connection conn=null;	
     java.sql.Statement stmt = null;
			
	public static void main(String[] args) 
	{
		
		hadoop_Training4_CATCH demo = new hadoop_Training4_CATCH();
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
		       bw = new BufferedWriter(new FileWriter(demo.non_logged_file_path));
		       bw.close();
		       
		} catch (IOException e1) 
		  {
		
			e1.printStackTrace();
		  }
              
		
		try {
			BufferedReader br =  new BufferedReader(new FileReader(demo.listing_file_path));
			String file_name =  br.readLine();
			while(br!=null)
			{
				System.out.println("Parsing File="+file_name);
				int len = (file_name.split("\\\\")).length;
				demo.package_name = file_name.split("\\\\")[len-2];
				demo.temp_file_path= file_name.replace("\\", "\\\\");
				//demo.id = 0;
				demo.ast_prser(file_name);
				file_name =  br.readLine();
				//br=null;
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
	
public void ast_prser(String file_name)
	{
		try
		{
			
			rawContent = hadoop_Training4_CATCH.readFileToString(file_name);
			
		}catch(Exception e){
			System.out.println( );
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
	    	               // System.out.println("import =" + id.getName().getFullyQualifiedName());
	    	                return false;
	    	                }

	    	               public boolean visit(VariableDeclarationFragment node) 
	    	                { 
	    	                 SimpleName name = node.getName();
	    	                // System.out.println("var.declaration =" + (name.getFullyQualifiedName() + ":" + cu.getLineNumber(name.getStartPosition())));
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
	    	        	      	  //method_content =  method_content.replaceAll("\"", " ");
	    	        	      	  //method_content =  method_content.replaceAll("\'", " ");
	    	        	      	  method_content = method_content.replaceAll("<\\?>","   ");// @@@ Note adding two spaces for reason because it was giving error  //
	    	        	      	  															//    in some cases if use  single space here " tr } } "             //
	    	        	      	  method_content = method_content.trim();
	    	        	      	 
	    	        	      	  //myblock = myblock.replaceAll("\\?","a");
	    	        	      	 //System.out.println("Myblock="+myblock);
	    	        	          methodVisitor(myblock);
	    	        	          //visitMethodDeclarion(myblock);
	    	        	      	}catch(java.lang.NullPointerException jnull)
	    	        	      	{
	    	        	      		System.out.println("Method Body is NULL");
	    	        	      		jnull.printStackTrace();	
		    	        	      	reset_parameters();
		    	        	        //write_in_db(0, ,"","");
	    	        	      	}
	    	                   catch(Exception e)
	    	                   {
	    	                    e.printStackTrace();
	    	                    reset_parameters();
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
         	
         return true;
        }
                
        
        public boolean visit(TryStatement mytry) 
        {
        	reset_try_flags();
        	util4_met utm = new util4_met();
        	log_level_interface tli = new log_level_interface();
        	method_name_and_count mnc_try =  new method_name_and_count();
        	operator_and_operator_count oaoc_try = new operator_and_operator_count();
        	
        	//object for method
        	method_name_and_count mnc_till_try =  new method_name_and_count();
        	operator_and_operator_count oaoc_till_try = new operator_and_operator_count();
        	log_level_interface tli_till_try = new log_level_interface();
        	
        	try_id++; 
        	catch_id= 0;
        	
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
        	
        	String try_con =  mytry.getBody().toString();
           /*try_con = try_con.replace("\"", "\\\"");
        	try_con= try_con.replace("\'", " ");
        	try_con = try_con.replace("\\\\", " ");*/
        	
        	/*try_con = try_con.replace("\"", " ");
        	try_con= try_con.replace("\'", " ");
        	try_con = try_con.replace("\\", " "); */
        	System.out.println("Content of Try Block=" + try_con.toString() );
        	
        	
        //	try_loc = utm.get_try_loc_count(try_con);// old method @not comment
        	try_loc= utm.get_new_try_sloc(try_con);
        	
        	tli = utm.find_and_set_logging_level(try_con, tli);
        	is_try_logged  = tli.logged;
        	try_log_count =  tli.log_count;
        	try_log_levels = tli.log_levels_combined;
        	is_return_in_try=utm.check_return(try_con);
        	is_thread_sleep_try = utm.check_thread_sleep(try_con);
        	throw_throws_try = utm.check_thorw_throws(try_con);
        	if_in_try = utm.check_if(try_con);
        	if_count_in_try = utm.get_if_count(try_con);
        	is_assert_try = utm.check_assert(try_con);
        	
        	mnc_try= utm.get_method_call_name(try_con, mnc_try);
        	method_call_names_try = mnc_try.method_names;
        	method_call_count_try =  mnc_try.method_count ;
        	
        	oaoc_try  =  utm.get_operators_and_count(try_con , oaoc_try);
        	operators_in_try = oaoc_try.operator;
        	operators_count_try = oaoc_try.operator_count;
        	
        	// @Uses: This function will use to count the variable names in the try block
        	// *** This will set following varibels in the block  //
        	//*** Variables_in_try =
        	//**  variables_count_try =           //
           	VariableVisitor_try(try_con);        
        	
        	//@******Features between method and try************@//

        	//interupt();
            int try_pos = mytry.getStartPosition();
            
            String method_try_between_con = method_content.substring(0, try_pos);
            method_try_between_con  =  method_try_between_con.trim();
            method_try_between_con  =  utm.balance_closing_braces(method_try_between_con);
            
            //System.out.println("method  con:"+ method_content);
            
            //System.out.println("method between try con:"+ method_try_between_con);
        
            String modified_con_for_method_call_ext  = utm.get_modified_con_for_method_cal_extraction(method_content , try_pos);
            mnc_till_try =  utm.get_method_call_name(modified_con_for_method_call_ext, mnc_till_try);
        	method_call_names_till_try  = mnc_till_try.method_names;
        	method_call_count_till_try  = mnc_till_try.method_count;
            
        	oaoc_till_try  =  utm.get_operators_and_count(method_try_between_con , oaoc_till_try);
        	operators_till_try = oaoc_till_try.operator;
        	operators_count_till_try = oaoc_till_try.operator_count;
        	
        	// @Uses: This function will use to count the variable names in the given block between method and try
        	// *** This will set following varibels in the block  //
        	//*** Variables_till_try =
        	//**  variables_count_till_try =           //
        	VariableVisitor_method_try_between_con(method_try_between_con);   
        	
        	loc_till_try = utm.get_new_sloc(method_try_between_con);          	    	
        	tli_till_try = utm.find_and_set_logging_level(method_try_between_con, tli_till_try);
        	is_till_try_logged  = tli_till_try.logged;
        	till_try_log_count =  tli_till_try.log_count;
        	till_try_log_levels = tli_till_try.log_levels_combined;
        	is_return_till_try     = utm.check_return(method_try_between_con);
        	throw_throws_till_try  = utm.check_thorw_throws(method_try_between_con);
        	if_in_till_try         = utm.check_if(method_try_between_con);
        	if_count_in_till_try   = utm.get_if_count(method_try_between_con);
        	is_assert_till_try     = utm.check_assert(method_try_between_con);
        	
        	
        	String previous_catch_as_string= "";
        	List all_catches =  mytry.catchClauses();
        	Iterator itr = all_catches.iterator();
        	
           	int count = 0;
        	
        	while(itr.hasNext())
        	{
        		
        		
        		log_level_interface catch_li = new log_level_interface();
        		catch_id++;
        		reset_catch_flags();
        		CatchClause mycatch = (CatchClause)all_catches.get(count);
    	
        				
    			 	String catch_exp = mycatch.getException().getType().toString();
    	           	catch_exp = catch_exp.replace("\"", "\\\"");
    	         	catch_exp = catch_exp.replace("\'", " ");
    	         	catch_exp = catch_exp.replace("\\\\", " ");
    	         	catch_exp  = utm.find_final_catch_exp(catch_exp);
    	         	
    	         	String catch_exp_with_obj = mycatch.getException().toString();    	         	
    	         	
    	         	String catch_con = mycatch.getBody().toString();
    	           	//catch_con = catch_con.replace("\"", "\\\"");
    	          	//catch_con = catch_con.replace("\'", "\\\'"); 
    	          	//catch_con = catch_con.replace("\\\\", " "); 
    	           
    	          	System.out.println("---------Set Flags----------------------");
    	            catch_li = utm.find_and_set_logging_level(catch_con, catch_li);
    	            is_catch_logged = catch_li.logged;
    	            catch_log_count = catch_li.log_count;
    	            catch_log_levels = catch_li.log_levels_combined;
    	            have_previous_catches= utm.contains_previous_catches(count);
    	            previous_catches_logged=utm.are_previous_catches_logged(previous_catch_as_string,count);
    	            previous_catches_log_count = utm.get_log_count(previous_catch_as_string);
                    is_return_in_catch= utm.check_return(catch_con);
                    catch_depth = utm.get_catch_depth(count);
                    is_catch_object_ignore=utm.check_ignore(catch_exp_with_obj);
                    is_interrupted_exception = utm.check_interrupted_exception(catch_exp);
                    is_throwable_exception = utm.check_throwable_exception(catch_exp);
                    throw_throws_catch = utm.check_thorw_throws(catch_con);
                    is_assert_catch= utm.check_assert(catch_con);
                    
    	         	System.out.println("Method Name="+ method_name);
    	         	System.out.println("Package Name="+ package_name);
    	         	System.out.println("File Path="+ temp_file_path);
    	         	System.out.println("Catch Exception="+catch_exp);
    	         	
    	    
    	         	
    	 	        write_in_db(try_id, catch_id,try_con,catch_con,method_try_between_con, catch_exp.toString(),previous_catch_as_string,temp_file_path,package_name, class_name, method_name, try_loc, 
    	 	        		is_try_logged, try_log_count, try_log_levels, is_catch_logged, catch_log_count, catch_log_levels, have_previous_catches, previous_catches_logged,
    	 	        		is_return_in_try, is_return_in_catch, is_catch_object_ignore, is_interrupted_exception, is_thread_sleep_try,throw_throws_try, 
    	 	        		throw_throws_catch,if_in_try, if_count_in_try, is_assert_try,is_assert_catch, previous_catches_log_count, catch_depth, is_method_have_param, method_param_as_string_original,
    	 	        		method_param_as_string,method_param_type, method_param_name, method_param_count,method_call_names_try, 	method_call_count_try, operators_in_try, operators_count_try, variables_in_try, variables_count_try,
    	 	        		method_call_names_till_try, method_call_count_till_try, operators_till_try, operators_count_till_try, variables_till_try, variables_count_till_try , loc_till_try,
    	 	        		is_till_try_logged ,  till_try_log_count ,  till_try_log_levels, is_return_till_try , throw_throws_till_try, if_in_till_try, if_count_in_till_try, is_assert_till_try);
    	 	          	 	        
    	 	       write_in_file(catch_con, catch_exp.toLowerCase(), try_con, previous_catch_as_string,  method_content, catch_log_count, method_param_as_string);
    	 	      
    	 	      	//not perfect
    	 	      	String catch_con_with_exp = mycatch.toString();
    	 	      	catch_con_with_exp =  catch_con_with_exp.replace("\"","\\\\");
    	 	      	catch_con_with_exp = catch_con_with_exp.replace("\'", " ");
    	 	      	catch_con_with_exp = catch_con_with_exp.replace("\\\\", " ");
        			previous_catch_as_string = previous_catch_as_string +"\n" + catch_con_with_exp;
        			
        		System.out.println("=========================================================================================================================");	
        		count++;
        		itr.next();
        	}
        	       
        	return true;
        }
       
        public boolean visit(CatchClause mycatch)
        {
        	String catch_train_con = "" ;
            return true; 
        }

    }
    );
}


//******@Uses: This is a small visitor created to extract variable name and count****************// 
//*********************** from a given try block ********************************************//
//***********************************************************************************************//
public void VariableVisitor_try(String content) 
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
        	variables_in_try       =  variables_in_try +" " + var.getName().getFullyQualifiedName();
        	variables_count_try    =  variables_count_try +1;
        	
        	System.out.println("Vriables in try:"+ variables_in_try);
            // interupt();
             return true;
            
        }

    }
    );
}


//******@Uses: This is a small visitor created to extract variable name and count****************// 
//*********************** from a given method try between content ****************************************//
//***********************************************************************************************//

public void VariableVisitor_method_try_between_con(String content) 
{
	System.out.println("I am in visitor:");
	ASTParser metparse = ASTParser.newParser(AST.JLS3);
    metparse.setSource(content.toCharArray());
    metparse.setKind(ASTParser.K_STATEMENTS);
    Block block = (Block) metparse.createAST(null);
	
    block.accept(new ASTVisitor() 
    {
          public boolean visit(VariableDeclarationFragment var) 
          {
        	  System.out.println(" hello"); 
           // debug("met.var", var.getName().getFullyQualifiedName());
        	variables_till_try       =  variables_till_try +" " + var.getName().getFullyQualifiedName();
        	variables_count_till_try =  variables_count_till_try +1;
        	
        	System.out.println("Vriables till try:"+ variables_till_try);
           // interupt();
            
            return true;
            
        }

    }
    );
} 

public void interupt()
{
	 BufferedReader br =  new BufferedReader(new InputStreamReader (System.in));
     try 
        {
         	br.readLine();
        }catch(Exception e)
     {
        	
     }
}
public void reset_parameters()
{
	log_levels_combined="";
	log_count =0;
	//method_name = "";
	//method_content="";
	//class_name= "";
}
	

public void reset_catch_flags()
{
	is_catch_logged = 0;
	catch_log_count = 0;
	catch_log_levels = "";
	have_previous_catches=0;
	previous_catches_logged=0;
	is_return_in_catch=0;
	is_interrupted_exception= 0;
	is_throwable_exception=0;
	throw_throws_catch = 0;
	is_assert_catch=0;
    previous_catches_log_count=0;
    catch_depth = 0;
}

public void reset_try_flags()
{
	try_loc = 0;
	is_try_logged  = 0;
	try_log_count =  0;
	try_log_levels = "";
	is_return_in_try=0;
	is_thread_sleep_try = 0;
	throw_throws_try = 0;
	if_in_try=0;
	if_count_in_try = 0;
	is_assert_try = 0;
	method_call_names_try = "";
	method_call_count_try=0;
	operators_in_try = "";
	operators_count_try = 0;
	variables_in_try = "";
	variables_count_try=0;
	
	//features between starting of method and try block
	method_call_names_till_try ="";
	method_call_count_till_try =0;
	operators_till_try    = "";
	operators_count_till_try = 0;
	variables_till_try = "";
	variables_count_till_try=0;
	loc_till_try=0;
	is_till_try_logged  = 0 ;
	till_try_log_count = 0;
	till_try_log_levels = "";
	is_return_till_try=0; 
	throw_throws_till_try = 0; 
	if_in_till_try = 0;
	if_count_in_till_try = 0 ;
	is_assert_till_try =0;
}

/*******************************************************This function is changed to handle the needs of cross project logging prediction*********************************************/
public void write_in_db(int try_id, int catch_id, String try_con, String catch_con,String method_try_between_con ,String catch_exception, String previous_catch_con, String file_path, 
		String package_name, String class_name, String method_name,int try_loc, int is_try_logged, int try_log_count, String try_log_levels, int is_catch_logged, int catch_log_count, String catch_log_levels,
		int have_previous_catches,int previous_catches_logged, int is_return_in_try, int is_return_in_catch, int is_catch_object_ignore, int is_interrupted_exception, int is_thread_sleep_try,
		 int throw_throws_try, int throw_throws_catch, int if_in_try, int if_count_in_try,int is_assert_try, int is_assert_catch, int previous_catches_log_count, int catch_depth, 
		int is_method_have_param, String method_param_as_string_original, String method_param_as_string, String method_param_type, String method_param_name, int method_param_count , 
		String method_call_names_try,  	int 	method_call_count_try, String operators_in_try, int operators_count_try,
		String variables_in_try, int variables_count_try, String method_call_names_till_try,  int method_call_count_till_try, String operators_till_try , int  operators_count_till_try,
		String variables_till_try, int varaibles_count_till_try, int loc_till_try, int is_till_try_logged , int  till_try_log_count , String till_try_log_levels, 
		int is_return_till_try , int throw_throws_till_try, int if_in_till_try, int if_count_in_till_try, int is_assert_till_try)
      {
	    
	     util4_met  utm =  new util4_met();	  
	    // method_content = utm.replace_quotes_string(method_content);
	     method_try_between_con = utm.replace_quotes_string(method_try_between_con);
	     try_con = utm.replace_quotes_string(try_con);
	     catch_con =  utm.replace_quotes_string(catch_con);
	     
	       
	     
	
      String insert_str= "insert into "+table+" values("+try_id+"," + catch_id+",\""+try_con+"\",\""+ catch_con+"\",\""+method_try_between_con+"\",\""+catch_exception+"\",\""+previous_catch_con+
       "\",\""+file_path+"\",\""+package_name+"\",\""
      +class_name+"\",\""+method_name+"\","+try_loc+","+is_try_logged+","+try_log_count+",\""+try_log_levels+"\","+is_catch_logged+","+catch_log_count+",\""+catch_log_levels+"\","+have_previous_catches+
      ","+ previous_catches_logged+","+is_return_in_try+","+is_return_in_catch+","+is_catch_object_ignore+","+is_interrupted_exception+","+is_thread_sleep_try
      +","+throw_throws_try+","+throw_throws_catch+","+if_in_try+","+if_count_in_try+","+is_assert_try+","+is_assert_catch+","+previous_catches_log_count+","+ catch_depth+","+
      is_method_have_param+",\""+method_param_as_string_original+ "\",\""+method_param_as_string+"\",\""+ method_param_type+"\",\""+method_param_name+"\","+method_param_count+",\""+method_call_names_try+"\","	+ method_call_count_try+",'"+operators_in_try+"',"+ operators_count_try  
      +",'"+ variables_in_try+"',"+ variables_count_try+",'"+ method_call_names_till_try+"',"+ method_call_count_till_try+",'"+operators_till_try+"',"+  operators_count_till_try+
      ",'"+ variables_till_try+"',"+ varaibles_count_till_try+","+ loc_till_try+","+ is_till_try_logged +","+  till_try_log_count +",\""+  till_try_log_levels+"\","+
      is_return_till_try +","+ throw_throws_till_try+","+ if_in_till_try+","+ if_count_in_till_try+","+ is_assert_till_try+")";
     
     System.out.println("Insert str="+insert_str);
     System.out.println("Try id="+try_id);
    
    /*if("ajp".equalsIgnoreCase(package_name))
 	{
 		System.out.println("Hi for ajp:"+ package_name);
 		//interupt();
 		
 	}*/
    try 
	{
		
    	    System.out.println("I am writing");
			stmt =  conn.createStatement();
			stmt.executeUpdate(insert_str);
	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
    
    
}

public void write_in_file(String catch_block, String expr_type, String try_con, String all_catch_as_string, String method_content, int log_count, String method_parameter)
{
	//log_count=1;
    //id++;
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
				// TODO Auto-generated catch block
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
   
    
    
    /* String insert_str= "insert into "+table+" values(\""+package_name+"\",\""+class_name+"\",\""+method_name+"\","+  id+",\""+method_content+"\",\""+
    log_levels_combined+"\",\""+temp_file_path+"\","+"\""+if_block+"\",\""+expr_type+"\","+log_count+",\""
    +if_train_con+"\","+logged+")";
    System.out.println("Insert str"+insert_str);
    */
       
    try 
    {
    	bw.write("File Path="+temp_file_path+"\n");
    	bw.write("Package Name ="+package_name+"\n");
    	bw.write("class name="+class_name+"\n");
    	bw.write("Method Name="+method_name+"\n");
    	bw.write("Try Content="+ try_con+"\n");
    	bw.write(" All Catch Blocks="+ all_catch_as_string+"\n");
        bw.write("method parameter = "+ method_parameter +"\n");
    	bw.write("method content="  +method_content+"\n");
        bw.write("--------------------\n");
		bw.close();
	} catch (IOException e) 
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



/*
 
 public boolean visit(CatchClause mycatch)
        {
        	String catch_train_con = "" ;
        	//String parent_content= myif.getParent().toString();
        	 //System.out.println(" parent_content"+parent_content);
             //String mod_method_con = method_content.replaceAll("\n", " ");
             //mod_method_con = method_content.replaceAll("\\s+", " ");
             
             //String mod_myif =  myif.toString().replaceAll("\n", " ");
             //mod_myif =  myif.toString().replaceAll("\\s+", " ");
            
        	////* **  @@ Not a comment
        	 int catch_pos = mycatch.getStartPosition();
             catch_train_con = method_content.substring(0, catch_pos);
             //catch_train_con = catch_train_con.replace("\n", " "); //make it comment for parsing
           	 catch_train_con = catch_train_con.replace("\"", "\\\"");
           	 catch_train_con = catch_train_con.replace("\'", "\\\'"); 
          	 catch_train_con = catch_train_con.replace("\\\\", " ");
          	// catch_train_con = catch_train_con.replaceAll("\n", " "); //make it comment for parsing
             catch_train_con = catch_train_con.replaceAll("\\s+", " ");
            @@@ Not a comment endhere ///
       	
           
            // System.out.println("catch VALUE="+catch_train_con+" ENDcatch");
             ///*
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
              
             }///
             
            // catch_train_con = catch_train_con.replaceAll("\n", " ");
             //catch_train_con =catch_train_con.replaceAll("\\s+", " ");
        	
             
             ///*  @@@@ Not a comment
            
            String catch_exp = mycatch.getException().getType().toString();
         	catch_exp = catch_exp.replace("\n", " ");
         	catch_exp = catch_exp.replace("\"", "\\\"");
         	catch_exp = catch_exp.replace("\'", " ");
         	catch_exp = catch_exp.replace("\\\\", " ");
         	
         	String catch_block = mycatch.toString();
         	catch_block = catch_block.replace("\n", " ");
          	catch_block = catch_block.replace("\"", "\\\"");
          	catch_block = catch_block.replace("\'", "\\\'"); 
          	catch_block = catch_block.replace("\\\\", " "); 
            System.out.println("-------------------------------");
          	System.out.println("Class Name="+ class_name);
         	System.out.println("Method Name="+ method_name);
         	System.out.println("Package Name="+ package_name);
         	System.out.println("File Path="+ temp_file_path);
         	System.out.println("Catch Exception="+catch_exp);
          	find_and_set_logging_level(mycatch.toString());
 	        insert(catch_block,catch_exp.toString(),catch_train_con);
 	      	reset_parameters();	
 	      	@@@ Not a comment  end here///
        	
          return true; 
        }
 
  */
 