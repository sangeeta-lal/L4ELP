


/*@Author
 * @Uses: 
 * 1. This program will compute the avearge performance of all the classifiers 9 base classifiers + 8 Bagging + 466 Avg voting + 466 maority Voting
 * 2. This program computes average with respect to the six pairs of project   i.e., result of each classifier is computed for the six pairs and their average is reported
 * */
public class compute_avg_performance_all_classifiers 
{

	
	/*
	String user_name =  "sangeetal";
	String password = "sangeetal";
	String url = "jdbc:mysql://localhost:3307/";
	String driver = "com.mysql.jdbc.Driver"; 
	String classifier_name = "";
	 
	// */

	///*
	String user_name =  "root";
	String password = "1234";
	String url = "jdbc:mysql://localhost:3306/";
	String driver = "com.mysql.jdbc.Driver";
	String classifier_name="";
	//*/

	
	String database = "logging4_elp";
	String cross_project_simple = "cross_log_pred_catch_simple";
	String cross_project_bagging_table = "cross_log_pred_bagging_catch";
	String cross_project_avg_vote_table = "cross_log_pred_avg_voting_catch_1_itr";
	String cross_project_max_vote_table ="cross_log_pred_max_voting_catch_1_itr";
	
	String results_table = "avg_performance_over_all_source_target_pairs_1_itr";
			
	
	
	
	
	private void get_and_insert(String cross_project_simple)
	{
		String base_str = " select  classifier, AVG(avg_precision), AVG(avg_recall), AVG(avg_fmeasure), AVG(avg_accuracy), AVG(avg_roc_auc) from "+ cross_project_simple +"   group by classifier"; 
		
		Connection
	  
		
	}
	
	
	
	public static void main(String args[])
	{
		
		compute_avg_performance_all_classifiers  cap =  new compute_avg_performance_all_classifiers ();
		cap.get_and_insert(cap.cross_project_simple);
	}


	
}
