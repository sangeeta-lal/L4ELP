
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;



/*@Author: Sangeeta
 * @Uses: This progtam is crated to provide utility methods to all classes in java
 * */


public class util4_met 
{

	public void print_hello()
	{
		System.out.println("Hello to new way of coding ");
	}
	
	public int get_try_loc_count(String con)
	{
		//@Comment: This is a specialized method for counting for try block as it counts loc =  loc-2 , because of bracket problem
		int loc = 0;
		
		String loc_arr[] = con.split("\n");
		for(int i = 0; i< loc_arr.length; i++)
		{
			if(is_blank_line(loc_arr[i])!=true)
			{
				loc++;
			}
		}
		
		if(loc!=0)
		  {loc = loc-2;}
		
		return loc;
		
	}
	
	
	// This function computes the SLOC of try bloc
	public int get_new_try_sloc(String con)
	{
		int sloc = 0;
		 Remove_Comments rc =  new Remove_Comments();
		   	
			String content_without_comment  =  rc.remove_comments(con);	
			String content_lines [] =  content_without_comment.split("\n");
			int length = content_lines.length;
			
			;
			for(int i=0; i<length;i++)
			{
				String line_val ="";
		        line_val= content_lines[i];
			    line_val = line_val.trim();
				    
			      if((is_blank_line(line_val)!=true)&&(is_import_or_package_stmt(line_val)!=true)&&(is_brace_only(line_val)!=true))
			      {
			    	// System.out.println("Line Val ="+ line_val);
			    	  sloc++;
			    	 // System.out.println("File SLOC ="+ final_file_sloc);
			    	  
			      }   //if blank
					 		
						 
			}// for loop
			
			return sloc;
		
		
	}

	
	
	//This function is used for counting LOC 
	public int get_loc(String con)
	{
		int loc=0;
		String loc_arr[] = con.split("\n");
		for(int i = 0; i< loc_arr.length; i++)
		{
			if(is_blank_line(loc_arr[i])!=true)
			{
				loc++;
			}
		}
			
		return loc;
	}
	
	public log_level_interface find_and_set_logging_level(String string_content, log_level_interface l) 
	{
		
		  // log_count=0;
			Pattern pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]+\\.info\\()");
			Matcher matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+"  "+(matcher.group(1).split("\\.")[1]).split("\\(")[0];
		      
			}	
			
		
			
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]+\\.trace\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+(matcher.group(1).split("\\.")[1]).split("\\(")[0];
			} 
			
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]+\\.debug\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+(matcher.group(1).split("\\.")[1]).split("\\(")[0];;
			} 
			
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]+\\.warn\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+(matcher.group(1).split("\\.")[1]).split("\\(")[0];;
			} 
			
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]+\\.error\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+(matcher.group(1).split("\\.")[1]).split("\\(")[0];;
			} 
			
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]+\\.fatal\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+(matcher.group(1).split("\\.")[1]).split("\\(")[0];;
			} 
			
			
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logInfo\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"info";
		      
			}	
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logI\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"info";
		      
			}
			

			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logTrace\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"trace";
		      
			}	
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logT\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"trace";
		      
			}
			

			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logDebug\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"debug";
		      
			}	
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logD\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"debug";
		      
			}

			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logWarn\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"warn";
		      
			}	
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logW\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"warn";
		      
			}

			/*PAT:   logErrorMessage(logFile,e);*/
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logError\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"error";
		      
			}	
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logE\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"error";
		      
			}
			
			
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logFatal\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"fatal";
		      
			}	
			pat = Pattern.compile("([a-zA-Z0-9_\\(\\)]*logF\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Start Index ="+matcher.start());	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));*/
		      l.log_levels_combined=l.log_levels_combined+" "+"fatal";
		      
			}
			//Pattern:  project.log(""wrong object reference "" + refId + "" - ""+ pref.getClass());
			/*pat = Pattern.compile("([a-zA-Z0-9_]+\\.log\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  System.out.println("Pat 1:");	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));
		      
		      l.log_levels_combined=l.log_levels_combined+" "+(matcher.group(1).split("\\.")[1]).split("\\(")[0];;
			} */
			
			//***Not Correct****Pattern: Logger.getLogger(getLoggerName(getHost(),url)).log(Level.WARNING,""Unable to determine web application context.xml "" + docBase,e);
			
			/*pat = Pattern.compile("([a-zA-Z0-9_]+\\(.*\\)\\.log\\()");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  System.out.println("Pat 2 :");	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(1));
		      
		      l.log_levels_combined=l.log_levels_combined+" "+(matcher.group(1).split("\\.")[1]).split("\\(")[0];;
			} */
			
			//pattern: log(""Error closing redirector: "" + ioe.getMessage(),Project.MSG_ERR)
		    
			//pat = Pattern.compile("\\{?[\\s\\n]*log\\(.*\\)");
			pat = Pattern.compile("[^\\(][\\s\n]*log\\(.*\\)");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  System.out.println("Pat 3:");	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(0));
		      
		      String level = get_non_standard_log_levels(string_content, matcher.start());		      
		      l.log_levels_combined=l.log_levels_combined+" "+ level;
		      
			}
			
			
			pat = Pattern.compile("log.append\\(.*\\)");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			 /* System.out.println("Pat 4:");	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(0));*/
		      
		      String level = get_non_standard_log_levels(string_content, matcher.end());		      
		      l.log_levels_combined=l.log_levels_combined+" "+ level;
			}
			
			
			pat = Pattern.compile("getLogWriter().println\\(.*\\)");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Pat 5:");	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(0));*/
		      
		      String level = get_non_standard_log_levels(string_content, matcher.end());		      
		      l.log_levels_combined=l.log_levels_combined+" "+ level;	
		     // System.out.println("HelloLevel");
			  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				try 
				 {
				 // br.readLine();
				 }catch(Exception e)
				{}
		     }
			
			pat = Pattern.compile("logWriter.println\\(.*\\)");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Pat 6:");	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(0));*/
		      
		      String level = get_non_standard_log_levels(string_content, matcher.end());		      
		      l.log_levels_combined=l.log_levels_combined+" "+ level;
			}	
			
			
			//pat:    doLog(  uri, response)
			pat = Pattern.compile("doLog\\s*\\(.*\\)");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Pat 7:");	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(0));*/
		      
		      	      
		      l.log_levels_combined=l.log_levels_combined+" "+ "Unknown1";
			}	
			
			
			
			//pat:     logIOException( Error reading root log dir this deletion   +  attempt is being aborted ,e);	
			pat = Pattern.compile("^\\s*log[a-zA-Z_]+\\s*\\(.*\\)");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Pat 7:");	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(0));*/
		      
		      	      
		      l.log_levels_combined=l.log_levels_combined+" "+ "Unknown2";
			}	
			
					
			
			//pat:  RumitLogger.logIOException( Error reading root log dir this deletion   +  attempt is being aborted ,e);	
			pat = Pattern.compile("[A-Za-z_\\(\\)\\.]+\\.\\s*log[a-zA-Z_]+\\s*\\(.*\\)");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Pat 7:");	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(0));*/
		      
		      	      
		      l.log_levels_combined=l.log_levels_combined+" "+ "Unknown3";
			}					
			
			//pat:    logPermissions();
			pat = Pattern.compile("logPermissions\\s*\\(.*\\)");
			matcher = pat.matcher(string_content);
			while(matcher.find())
			{
			  /*System.out.println("Pat 7:");	
			  System.out.print("Start index: " + matcher.start());
		      System.out.print(" End index: " + matcher.end() + " ");
		      System.out.println("pattern matched = "+matcher.group(0));*/
		      
		      	      
		      l.log_levels_combined=l.log_levels_combined+" "+ "Unknown5";
			}					
			
			
			if(l.log_levels_combined!="")
			{
			l.log_count= l.log_levels_combined.trim().split(" +").length;
			l.logged = 1;
			}
			System.out.println("Final Log levels are:"+l.log_levels_combined);
			
			return l;
	}

	private String get_non_standard_log_levels(String string_content, int start_index)
	{
		String level = "";
		
		int end_index= string_content.indexOf(")", start_index);
		String substring = string_content.substring(start_index, end_index);
		
		//System.out.println("Substring oitside="+ substring);
		int index =  substring.indexOf("Level");
		//System.out.println("level index=" +index);
		if(index==-1)
		{
			level = "Unknown5";
			//System.out.println("HelloLevel");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try 
			 {
				//System.out.println("string content="+ string_content+" substring="+substring);
			    //br.readLine();
			 }catch(Exception e)
			{}
			return level;
		}
		else
		{
			//level= "hello";
			//System.out.println("I am here");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try 
			 {
			  // br.readLine();
			 
			
			//System.out.println("Helljkjklj");
			String  substring_part[]  =  substring.split(",");
			//System.out.println("xyz"+substring_part[0]+"xyz");
			String temp_level2[] =  substring_part[0].split("\\.");
			level = temp_level2[1];
			if(level.equalsIgnoreCase("WARNING"))
			{
				level = "warn";
			}
			
			//System.out.println("level = "+ level);
			
			 }catch(Exception e)
			{ 
				e.printStackTrace();
			}
		//	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try 
			 {
			   //br.readLine();
			 }catch(Exception e)
			{}
			return level;
		}
		
	}


	public int contains_previous_catches(int count)
	{
		if(count==0)
			return 0;
		
		else
		return 1;
	}

	public int are_previous_catches_logged(String previous_catch_as_string, int count) 
	{
		if(count==0)// first catch block
			return 0;
		
		log_level_interface temp  = new log_level_interface();
		find_and_set_logging_level(previous_catch_as_string, temp);
		
		int logged =  temp.logged;
		return logged;
	}
	
	public int get_log_count(String content) 
	{
	   int log_count = 0 ;
	   log_level_interface temp = new log_level_interface();
	   find_and_set_logging_level(content, temp);
	   log_count = temp.log_count;
		return log_count ;
	}


	public int check_return(String content)
	{

	  int contains_return_stmt=0;
	  //return  : space is added intetionalyy in the regEx
	   contains_return_stmt=	content.contains("return ")?1:0;
		
		return contains_return_stmt;
	}

	public int check_ignore(String catch_exp_with_obj)
	{
	
		catch_exp_with_obj= catch_exp_with_obj.toString().toLowerCase();
	
		int obj_contains_ignore=0;

		  // ignore : space is added intetionalyy in the regEx
		 
		obj_contains_ignore=catch_exp_with_obj.contains(" ignore")?1:0;
		return obj_contains_ignore;
	}

	public int check_interrupted_exception(String catch_exp)
	{
		if(catch_exp.equalsIgnoreCase("InterruptedException"))
		return 1;
		
		else 
			return 0;
	}

	public int check_thread_sleep(String try_con) 
	{
		int thread_sleep_try=0;
		thread_sleep_try= try_con.toLowerCase().contains("Thread.sleep".toLowerCase())?1:0;
		return thread_sleep_try;
	}

	public int check_throwable_exception(String catch_exp) 
	{
		if(catch_exp.equalsIgnoreCase("Throwable"))
			return 1;
			
			else 
			 return 0;
	}

	public int check_thorw_throws(String content) 
	{
		int contains_throw_throws = 0;
		content =  content.toString().toLowerCase();
		content =  content.replace("\n"," ");
		
	
		//check throw
		Pattern throw_pat = Pattern.compile(".*throw\\s+.*");
		Matcher m = throw_pat.matcher(content);
		if (m.find())
		{
		  contains_throw_throws = 1;
		}

		
		//check throws 
		if(contains_throw_throws==0)
		{

			throw_pat = Pattern.compile(".*throws\\s+.*");
			m = throw_pat.matcher(content);
			if (m.find())
			{
			  contains_throw_throws = 1;
			}
			
		}
			
		return contains_throw_throws;
	}

	public int check_if(String content)
	{
		int if_present=0;
		
		content =  content.toString().toLowerCase();
		content =  content.replace("\n"," ");
		
		//check if
		Pattern throw_pat = Pattern.compile(".*if\\s*\\(.*");
		Matcher m = throw_pat.matcher(content);
		if(m.find())
		{
			
			if_present =1;
		}
	
	 return if_present;
	}

	public int get_if_count(String content) 
	{
		int if_count  = 0;
		
		content =  content.toString().toLowerCase();
		
		//content =  content.replace("\n"," "); //@Eding \n causes error and code is not able to find all the ocuurances
		
		//check if
		Pattern if_pat = Pattern.compile(".*if\\s*\\(.*");
		Matcher m = if_pat.matcher(content);
		while(m.find())
		{
			
			//System.out.println(" Grop = "+m.group(0));
			if_count++;
		}
		
		return if_count;
	}

	public int check_assert(String content)
	{
		int contains_assert = 0;
		
		content = content.toLowerCase();
		contains_assert=	content.contains(".assert".toLowerCase())?1:0;
		/*if(contains_assert==0)
		{
			contains_assert=	content.contains(".assertFalse".toLowerCase())?1:0;
		}*/
		
		return contains_assert ;
	}

	public int get_catch_depth(int count)
	{
	
		return  count  +1;
	}

	public String find_final_catch_exp(String catch_exp) 
	{
		 String exp[] = catch_exp.split("\\.");
		 int len = exp.length;		 
		 catch_exp  = exp[len-1];
		 
		 return catch_exp;
		
	}

	public int check_method_parameter(List method_parameter)
	{
		
		
	  int is_param=  method_parameter.size()>0? 1 :0;
				
	  return is_param;
	}

	public int get_param_count(List method_parameter) 
	{
		
	
		int param_count  =0;
		
		param_count =  method_parameter.size();
		
		return param_count; 
	}

	public String replace_quotes_string(String input_string) 
	{
		input_string = input_string.replace("\"", " ");
		input_string = input_string.replace("\'", " ");
		input_string = input_string.replace("\\", " ");
		return input_string;
	}
	

	
	//This function will do augmentation of string for extracting function calls
	public String aug_for_method_call_extraction(String input)
	{
		String output = "";
		int index  =  input.indexOf("{");
		
		String input_substring =  input.substring(index+1);
				
		output= "class test { public static void main() {  " + input_substring + "  }";
		return output;
		
	}
	
	//@Uses: This method will be used to compute the 
	public method_name_and_count get_method_call_name(String input_con, method_name_and_count mnc) 
	{
		String method_call_names = "";
		int method_count = 0;	    
		String modified_input = " ";
		
		//Do augumentation for parsng method call
		modified_input  =  aug_for_method_call_extraction(input_con);
		
		//=====New Lines===================================//
		Remove_Comments rc =  new Remove_Comments();
		modified_input = rc.remove_comments(modified_input);
		//================================================//
		
		MethodCallPrinterClass obj =  new MethodCallPrinterClass();
		method_call_names = obj.visitor(modified_input);
		
		//System.out.println(" method content debu:="+ modified_input);
		
		method_call_names= method_call_names.trim();
		String temp[]= method_call_names.split(" ");
		mnc.method_count = temp.length;
		mnc.method_names =  method_call_names;
	
		return mnc;
	
	}		
	
	
	//***************************************************************************************//
	// @uses: This program is used to find and count parameters in a given string block      //
	//***************************************************************************************//
	public operator_and_operator_count get_operators_and_count(String try_con, 	operator_and_operator_count oaoc_try) 
	{
		String operator = "";
		int operator_count = 0;
		
		Remove_Comments rc =  new Remove_Comments();// new line
		try_con =  rc.remove_comments(try_con); //new line
				
		//*****************************************************************************************************//
		//@This pattern 1: It can find operators in a give string  
		// operators: =, *,&, +, -, %,!, (), [],  &,? ,:,>, <, ^, ~
		//******************************************************************************************************//		
		Pattern pat = Pattern.compile("([=*+\\-%!\\(\\)\\[\\]&\\?:|><^~/])");
		Matcher matcher = pat.matcher(try_con);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try 
		 {
		  // br.readLine();
		 }catch(Exception e)
		{}
		while(matcher.find())
		{
			 // System.out.println("operator"+matcher.group(0)+ " operator count = "+ operator_count);
			  operator = matcher.group(0)+" "+operator;
			  operator_count++;
		}
		
		operator = operator.trim();
		oaoc_try.operator = operator;
		oaoc_try.operator_count = operator_count ; 
		return oaoc_try;
	}

	public String balance_closing_braces(String method_try_between_con)
	{
		int count_opening_braces = 0;
		int count_closing_braces = 0;
		int diff = 0;
		
		count_opening_braces= StringUtils.countMatches(method_try_between_con, "{");
        count_closing_braces = StringUtils.countMatches(method_try_between_con, "}");
        
        diff=   count_opening_braces   -  count_closing_braces;
        
       // method_try_between_con = method_try_between_con  +" {};";
        
        for(int  i = 0 ;i < diff; i++)
        {
        	method_try_between_con  =  method_try_between_con + " "+ "}"; //; can be added if required
        }
        
	    //System.out.println(" Number of brackets:"+count_opening_braces+ "  closing braces="+ count_closing_braces);
		return method_try_between_con;
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

	//@Uses: This method is used for cleaning parameters of method
	public String clean_method_params(String method_param_as_string) 
	{

		method_param_as_string=  method_param_as_string.replaceAll("[\\[\\].<>?,]", " ");
		method_param_as_string= method_param_as_string.replaceAll("\\s+", " ");
		
		return  method_param_as_string;
	}


//@Uses: This function checks whether the if condition have "null" condition in it or not.
public int check_null_condition(String if_expr) 
{ 
	int null_present=0;
	
	if_expr =  if_expr.toString().toLowerCase();
	if_expr =  if_expr.replace("\n"," ");
	
	//check if
	Pattern throw_pat = Pattern.compile("\\s*null");
	Matcher m = throw_pat.matcher(if_expr);
	if(m.find())
	{
		
		null_present =1;
	}

     return null_present;

	}
	

//@Uses: This function checks whether the if condition have "instanceOf" condition in it or not.
public int check_instanceOf_condition(String if_expr) 
{ 
	int instance_of_present=0;
	
	if_expr =  if_expr.toString().toLowerCase();
	if_expr =  if_expr.replace("\n"," ");
	
	//check if
	Pattern throw_pat = Pattern.compile("\\s*instanceof");
	Matcher m = throw_pat.matcher(if_expr);
	if(m.find())
	{
		
		instance_of_present =1;
	}

   return instance_of_present;

	}


//  Returns the method param type
public String get_method_param_type(List method_parameter)
{
	String method_param_type = "";
	
	Iterator iterator = method_parameter.iterator();
	
	while(iterator.hasNext())
	{  
		method_param_type = method_param_type  +  iterator.next().toString().split(" ")[0]+" ";
	}
	method_param_type  =  method_param_type.trim();
	return method_param_type;
}
	

//Returns the method param name
public String get_method_param_name(List method_parameter)
{
  String method_param_name = "";
  Iterator iterator = method_parameter.iterator();

  while(iterator.hasNext())
   {  
	  method_param_name = method_param_name  +  iterator.next().toString().split(" ")[1]+" ";
   }
  method_param_name  =  method_param_name.trim();
  return method_param_name;
}


// == This function is used in case you have a incomplete code in case of method_try_between_con or method_if_between_con ==
public String get_modified_con_for_method_cal_extraction( String method_content, int try_pos) 
{

	String part1= "", part2="";
	part1 =  method_content.substring(0, try_pos);
	part2 = method_content.substring(try_pos);
	
	String new_string = part1  + " sangeeta(); " + part2;
	return new_string;
}



// This function checks for blank lines in source-code
public boolean is_blank_line(String line_val)
{
	
	
	if(line_val.trim().isEmpty())
	{
		return true;
	}

 return false;
}

// This function checks whether a line contains only brace  "{" or "}"
public boolean is_brace_only(String line_val)
{
	
	
	if((line_val.trim().equalsIgnoreCase("{")) || (line_val.trim().equalsIgnoreCase("}")))
	{
		return true;
	}

 return false;
}

//This function checks for the  package or import statements
public boolean is_import_or_package_stmt(String line_val)
{
	
	
	if((line_val.trim().matches("import[ ]+(.*);")) || (line_val.trim().matches("package[ ]+(.*);")))
	{
		return true;
	}

 return false;
}



// This is the final method for computing sloc of a given file
// This function removes comments, packages, blank lines and lines containing braces only
public int find_final_file_SLOC(String file_content_as_string)
{
   Remove_Comments rc =  new Remove_Comments();
   	
	String content_without_comment  =  rc.remove_comments(file_content_as_string);	
	String content_lines [] =  content_without_comment.split("\n");
	int length = content_lines.length;
	
	int final_file_sloc = 0;
	for(int i=0; i<length;i++)
	{
		String line_val ="";
        line_val= content_lines[i];
	    line_val = line_val.trim();
		    
	      if((is_blank_line(line_val)!=true)&&(is_import_or_package_stmt(line_val)!=true)&&(is_brace_only(line_val)!=true))
	      {
	    	// System.out.println("Line Val ="+ line_val);
	    	  final_file_sloc++;
	    	 // System.out.println("File SLOC ="+ final_file_sloc);
	    	  
	      }   //if blank
			 		
				 
	}// for loop
	
	return final_file_sloc;
}


// This method will be used to create a new feature to check wthether a given file is logged or not
public int check_if_java_file_logged(String file_content_as_string) 
{

 int is_java_file_logged = 0; 
 util4_met u4m= new util4_met(); 
 log_level_interface l = new log_level_interface();
 Remove_Comments  rc =  new Remove_Comments();
 
 try{
 			String file_content_without_comment =  rc.remove_comments(file_content_as_string);	
 			l=  u4m.find_and_set_logging_level(file_content_without_comment, l);		
 			is_java_file_logged = l.logged;
 	     	
 		}catch(Exception e)
 		{
 			e.printStackTrace();
 		}
   return is_java_file_logged;
}

//Uses: This function reads the content of a file and store them in a string
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




}//class




/*
//@Uses: This method will be used to compute the 
public method_name_and_count get_method_call_name(String try_con, method_name_and_count mnc) 
{
	String method_call_name_try = "";
	int method_count = 0;
	
	//*****************************************************************************************************
	//@This pattern 1: Which can find all the type of methods such as 
	//asList((FeatureDescriptor[])pds).iterator() setValue(TYPE,pds[i].getPropertyType()) 
	//setValue(RESOLVABLE_AT_DESIGN_TIME,Boolean.TRUE) getPropertyDescriptors() getBeanInfo(base.getClass())
	//******************************************************************************************************	
	System.out.println(" ========in=================");
	Pattern pat = Pattern.compile("([a-zA-Z][0-9_a-zA-Z]*\\([a-zA-Z0-9_\\s,\\[\\]\\(\\)\\.]+\\))");
	Matcher matcher = pat.matcher(try_con);
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	try 
	 {
	   //br.readLine();
	 }catch(Exception e)
	{}

	while(matcher.find())
	{
		  String temp_method_name =  matcher.group(0).split("\\(")[0];
		  method_call_name_try = temp_method_name +" "+ method_call_name_try;
		  method_count++;
	}
	
	method_call_name_try = method_call_name_try.trim();
	System.out.println("Method in try names= "+ method_call_name_try);
	System.out.println(" ==out==");
	
	//****************************************************************************************************
	// @This pattern 2: It can extract all the functions having no parameter
	// Example : getClass()
	//****************************************************************************************************	
	pat = Pattern.compile("([a-zA-Z][0-9_a-zA-Z]*\\([\\s]*\\))");
	matcher = pat.matcher(try_con);
	while(matcher.find())
	{
		  String temp_method_name =  matcher.group(0).split("\\(")[0];
		  method_call_name_try = temp_method_name +" "+ method_call_name_try;
		  method_count++;
	}
	
	method_call_name_try = method_call_name_try.trim();
	System.out.println("Method in try names= "+ method_call_name_try);
	try
	 {
	 //  br.readLine();
	 }catch(Exception e)
	{}
	
					
	mnc.method_count = method_count;
	mnc.method_names =  method_call_name_try;

	return mnc;
}
*/

/*	
//@Uses: This method will be used to compute the 
public method_name_and_count get_method_call_name(String try_con, method_name_and_count mnc) 
{
	String method_call_name_try = "";
	int method_count = 0;
	

			
	//*********************************************
	//     Let me match everything               //
	//********************************************
	Pattern pat = Pattern.compile("(.*\\([.*]*)");
	Matcher matcher = pat.matcher(try_con);
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	while(matcher.find())
	{
		  String temp_method_name =  matcher.group(0).split("\\(")[0];
		  method_call_name_try = temp_method_name +" "+ method_call_name_try;
		  method_count++;
	}
	
	method_call_name_try = method_call_name_try.trim();
	System.out.println("Method in try names= "+ method_call_name_try);
	try
	 {
	 //  br.readLine();
	 }catch(Exception e)
	{}		
	
	
	mnc.method_count = method_count;
	mnc.method_names =  method_call_name_try;

	return mnc;
}
*/

/*
//@Uses: This method will be used to compute the 
public method_name_and_count get_method_call_name(String input_content, method_name_and_count mnc) 
{
	String method_call_name_try = "";
	int method_count = 0;
	
	String modified_content = " ";
	
	//Do augumentation for parsng method call
	modified_content  =  aug_for_method_call_extraction(input_content);
	
	// convert String into InputStream
	InputStream is = new ByteArrayInputStream(modified_content.getBytes());
	BufferedReader br = new BufferedReader(new InputStreamReader(is));        
    
    CompilationUnit cu = null;
    try
    {
        cu = JavaParser.parse(is);
	
    } catch (ParseException e)
    {
       e.printStackTrace();
	}
    
    finally
    {
        try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    new MethodVisitor().visit(cu, null);


}

// This is the function from 
 private static class MethodVisitor extends VoidVisitorAdapter
    {
        @Override
        public void visit(MethodCallExpr methodCall, Object arg)
        {
            System.out.print("Method call: " + methodCall.getName() + "\n");
            List<Expression> args = methodCall.getArgs();
            if (args != null)
                handleExpressions(args);
        }

        private void handleExpressions(List<Expression> expressions)
        {
            for (Expression expr : expressions)
            {
                if (expr instanceof MethodCallExpr)
                    visit((MethodCallExpr) expr, null);
                else if (expr instanceof BinaryExpr)
                {
                    BinaryExpr binExpr = (BinaryExpr)expr;
                    handleExpressions(Arrays.asList(binExpr.getLeft(), binExpr.getRight()));
                }
            }
        }
    }*/