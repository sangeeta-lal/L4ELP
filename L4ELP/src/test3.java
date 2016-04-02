

public class test3
{


  public static void main(String args[])
  {
	  
	  String s  = "   s_logger.info(log(seq, Unable to send due to   + e.getMessage())); \n "+
			  	"	cancel(seq);    throw e; }";
	  String s2= "  log( IOException sending message ,e);";
	  String s3 = "  res.setStatus(400); " +
			  	"	res.setMessage( Invalid URI:   + ioe.getMessage()); " +
			   "		connector.logAccess(request,response,0,true);  " +
			   "	return false;  ";
	
	  util3_met um = new util3_met();
	  log_level_interface l = new log_level_interface();
	//  l =um.find_and_set_logging_level(s7, l);
	  
	  String  str = "package                     logging3;     ;";
	  
	  System.out.println("  import="+um.is_import_or_package_stmt(str));
	  
	  
	  String str2= " before /* I amcomment "
	  		+ "*jj \n ******/ mid \n"
	  		+ " /*mmmm*/ uff"
	  		+ "// i am commet"
	  		+ " \n but i am code";
	  System.out.println(" old str2 = "+ str2);
	//  str2 =  str2.replaceAll("(/\\*([^*]|[\r\n]|(\\*([^/]|[\r\n])))*\\*/)", " \n");
	  str2 =  str2.replaceAll("(/\\*([^*]|[\r\n]|(\\*+([^*/]|[\r\n])))*\\*+/)|(//.*)", "\n");
	  System.out.println(" new str2 = "+ str2);
	  operator_and_operator_count oaoc_try = new  operator_and_operator_count();
//	System.out.println(" l="+ l.log_count+  "  levels="+ l.log_levels_combined+  um.get_operators_and_count(s7, oaoc_try).operator);
	  
  
  Remove_Comments cf =  new Remove_Comments();
  System.out.println(" new str = "+ cf.remove_comments(str2));
  
  }
}
