import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Stacking;
import weka.classifiers.meta.Vote;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.ADTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;

/* This file will be used to ensemble based prediction using voting of algorithms
 * 1. We are using two types of voting
 * 2. Majority voting and average voting
 * 3. This file will read list of best combinations for each file and will run-them for 10 iterations
 *  
 * 
 * 
 */

public class cross_log_pred_max_voting_best_10_itr
{

/*
String path = "E:\\Sangeeta\\Research\\";
String user_name =  "sangeetal";
String password = "sangeetal";
String url = "jdbc:mysql://localhost:3307/";
String driver = "com.mysql.jdbc.Driver"; 
String classifier_name = "";
String possible_comb_file_path=path +"L4ELP\\result\\comb";
String result_file =  path+"L4ELP\\result\\max_vote_result.txt";
 
// */

///*
String path = "F:\\Research\\";
String user_name =  "root";
String password = "1234";
String url = "jdbc:mysql://localhost:3306/";
String driver = "com.mysql.jdbc.Driver";
String classifier_name="";
String possible_comb_file_path=path +"L4ELP\\result\\best_comb";
String result_file =  path+"L4ELP\\result\\max_vote_result.txt";
//*/


String type = "catch";
//String type = "if";
int iterations=10;
String source_project="tomcat";
String target_project = "cloudstack";
//String target_project="hd";

//String source_project="cloudstack";
//String target_project = "tomcat";
//String target_project="hd";

//String source_project="hd";
//String target_project = "tomcat";
//String target_project="cloudstack";


String db_name ="logging4_elp";
String result_table = "cross_log_pred_max_voting_"+type+"_best_10";


String source_file_path = path+"L4ELP\\dataset\\"+source_project+"-arff\\catch\\complete\\"+source_project+"_catch_complete.arff";		
String target_file_path = path+"L4ELP\\dataset\\"+target_project+"-arff\\catch\\balance\\"+target_project+"_catch_balance";

String classifier_name_acronym = "";
DataSource trainsource;
DataSource testsource;
Instances trains;
Instances tests;
Evaluation result;

int instance_count_source = 0;
int instance_count_target =0;
//Connection conn=null;	
//java.sql.Statement stmt = null;


//This function uses dataset from the ARFF files
public void read_file(int i)
{ 
try 
	{
	
		trainsource = new DataSource(source_file_path);
		trains = trainsource.getDataSet();
		trains.setClassIndex(0);
		
		testsource = new DataSource(target_file_path+"_"+i+".arff");
		tests = testsource.getDataSet();
		
		tests.setClassIndex(0);
		
		instance_count_source = trains.numInstances();
		instance_count_target = tests.numInstances();
		
		//System.out.println("Instance count source ="+ instance_count_source + "  Instance count target="+ instance_count_target);
   
	} catch (Exception e) 
	{
	
		e.printStackTrace();
	}	  
	
}


//This function is used to pre-process the dataset
public void pre_process_data()
{

 try
   {
	 
	  
	  //1. TF-IDF
	  StringToWordVector tfidf_filter = new StringToWordVector();
	  tfidf_filter.setIDFTransform(true);
	  tfidf_filter.setInputFormat(trains);
	  trains = Filter.useFilter(trains, tfidf_filter);     	  
	
	  tests = Filter.useFilter(tests, tfidf_filter);
 
     

     //2. Standarize  (not normalize because normalization is affected by outliers very easily)   	  
	  Standardize  std_filter =  new Standardize();
	  std_filter.setInputFormat(trains);
	  trains= Filter.useFilter(trains,std_filter);     	  
	 
	  tests= Filter.useFilter(tests,std_filter);  	  
     

     //3. Discretizations
	  Discretize dfilter = new Discretize();
     dfilter.setInputFormat(trains);
     trains = Filter.useFilter(trains, dfilter);
     
     tests = Filter.useFilter(tests, dfilter);	      
     
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}


public Connection initdb(String db_name)
{
	Connection conn =  null;
	
try {
	      Class.forName(driver).newInstance();
	     conn = DriverManager.getConnection(url+db_name,user_name,password);
	      //System.out.println(" dbname="+ db_name+ "user name"+ userName+ " password="+ password);
	      if(conn==null)
	      {
	    	  System.out.println(" Database connection is null. Check it.");
	      }
	      
	 } catch (Exception e) 
	 {
	      e.printStackTrace();
	 }
	return conn;
}


//This method computes the average value  and std. deviation and inserts them in a db
public void compute_avg_stdev_and_insert(String classifier_name, int no_of_classifier,int comb_count, double[] precision, double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{

// computes following metrics:
	/*
	 * 1. Precision
	 * 2. Recall
	 * 3. Accuracy
	 * 4. F measure
	 * 5. ROC-AUC
	 * */

	double avg_precision = 0.0;
	double avg_recall = 0.0;
	double avg_accuracy = 0.0;
	double avg_fmeasure = 0.0;	
	double avg_roc_auc = 0.0;
	
	double std_precision = 0.0;
	double std_recall = 0.0;
	double std_accuracy = 0.0;
	double std_fmeasure = 0.0;	
	double std_roc_auc = 0.0;
	//double total_instances = 0.0;
	
	util4_met  ut = new util4_met();
	
	avg_precision   = ut.compute_mean(precision);
	avg_recall      = ut.compute_mean(recall);
	avg_fmeasure    = ut.compute_mean(fmeasure);
	avg_accuracy    = ut.compute_mean(accuracy);
	avg_roc_auc     = ut.compute_mean(roc_auc);
	
	std_precision   = ut.compute_stddev(precision);
	std_recall      = ut.compute_stddev(recall);
	std_fmeasure    = ut.compute_stddev(fmeasure);
	std_accuracy    = ut.compute_stddev(accuracy);
	std_roc_auc     = ut.compute_stddev(roc_auc);
	
		
  // System.out.println("model ="+classifier_name +"   Acc = "+ avg_accuracy + "  size="+ pred_10_db.size());
	
	String insert_str =  " insert into "+ result_table +"  values("+ "'"+ source_project+"','"+ target_project+"','"+ classifier_name+"',"+no_of_classifier+","+comb_count+","+ trains.numInstances() + ","+ tests.numInstances()+","
	                       + iterations+","+trains.numAttributes() +","+avg_precision+","+ std_precision+","+ avg_recall+","+ std_recall+","+avg_fmeasure+","+std_fmeasure+","+ avg_accuracy 
	                       +","+std_accuracy+","+ avg_roc_auc+","+ std_roc_auc+" )";
	System.out.println("Inserting="+ insert_str);
	
	
	write_inf_file(insert_str, result_file);
	
	Connection conn2 = initdb(db_name);
	Statement stmt2 =  null;
	if(conn2==null)
	{
		System.out.println(" Databasse connection is null");
		
	}
	
	try 
	{
		stmt2 = conn2.createStatement();
		stmt2.executeUpdate(insert_str);
		stmt2.close();
		conn2.close();
	} catch (SQLException e) {
		
		e.printStackTrace();
	}

}



// This file will be used to write result records in the file 
private void write_inf_file(String insert_str, String result_file2) 
{
try 
{
	BufferedWriter br =  new BufferedWriter(new FileWriter(result_file2,true));
	
	//if file does not exists create the file
	if(br==null)
	{
		System.out.println("Result file doesn not exist");
	}
	
	br.write(insert_str+";");
	br.write("\n");
    br.close();

} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	
}


//Insert maximum voting of multiple algorithms
private void learn_and_insert_max_voting(double[] precision, double[] recall,
		double[] accuracy, double[] fmeasure, double[] roc_auc, int no_of_classifier)
{
	System.out.println("Max Voting "+ no_of_classifier +" algorithms:"+ type);
	//\\=========== voting of many =================================//\\	
	
	
	//Read the file consisting of all the possible combinations
	possible_comb_file_path = possible_comb_file_path+"_"+source_project+"_"+target_project+"_"+"max_"+no_of_classifier+".txt";
	BufferedReader br= null;
	
	try 
	{
		br =  new BufferedReader(new FileReader(possible_comb_file_path));
	
	
	} catch (FileNotFoundException e) 
	{
		e.printStackTrace();
	}
	
	
	

	try {
		
		String classifier_comb_string =br.readLine();
	    int comb_count =1;
		while(br!=null)
		{
	  
	             System.out.println("com=" +classifier_comb_string);
	             
	             Classifier[] cfsArray = new Classifier[no_of_classifier]; 
	             cfsArray= get_classifier_array(no_of_classifier, classifier_comb_string);
				 
	             for(int i=0; i<iterations; i++)
					{
					 System.out.println("Iteration=" + i);
					 read_file(i+1);
			   
					 pre_process_data();
					 result = cross_pred_max_voting(cfsArray);				
				
					 precision[i]         =   result.precision(1)*100;
					 recall[i]            =   result.recall(1)*100;
					 accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
					 fmeasure[i]          =   result.fMeasure(1)*100;
					 roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
					//@ Un comment to see the evalauation results
					//System.out.println(clp.result.toSummaryString());							
				   }
				  
		   compute_avg_stdev_and_insert(classifier_name_acronym, no_of_classifier, comb_count, precision, recall, accuracy, fmeasure , roc_auc );
		   
		   classifier_comb_string =br.readLine();  
		   comb_count++;
	
	}//while

		br.close();
	
	} catch (IOException e) 
	{
		
		e.printStackTrace();
	}
	
}



private Classifier[] get_classifier_array(int no_of_classifier, String classifier_comb_string) 
{
	Classifier[] CfsArray =  new Classifier[no_of_classifier];
	classifier_name_acronym = "";
	
	for(int j=0; j<no_of_classifier; j++)
	{
		int classifier_type =  (classifier_comb_string.toCharArray())[j]-'0'-1;
		
		switch(classifier_type){
			case 0:
				 AdaBoostM1     cfs1   = new AdaBoostM1();
				 CfsArray[j]         = cfs1;
				 classifier_name_acronym =  classifier_name_acronym + "ADA-";
				 break;
			
			case 1:	
				 ADTree         cfs2 = new ADTree();
				 CfsArray[j]         = cfs2;
				 classifier_name_acronym =  classifier_name_acronym + "ADT-";
				break;
				
			case 2:
				 BayesNet       cfs3 = new BayesNet();	
				 CfsArray[j]         = cfs3;
				 classifier_name_acronym =  classifier_name_acronym + "BN-";
				 break;
				 
			case 3:
				 DecisionTable  cfs4 = new DecisionTable();
				 CfsArray[j]         = cfs4;
				 classifier_name_acronym =  classifier_name_acronym + "DT-";
				break;
			
			case 4:
				 J48            cfs5 = new J48();  
				 CfsArray[j]         = cfs5;
				 classifier_name_acronym =  classifier_name_acronym + "J48-";
				 break;
			
			case 5:
				 Logistic       cfs6 = new Logistic();
				 CfsArray[j]         = cfs6;
				 classifier_name_acronym =  classifier_name_acronym + "LOG-";
				break;
			
			case 6:
				 NaiveBayes     cfs7 = new NaiveBayes();
				 CfsArray[j]         = cfs7;
				 classifier_name_acronym =  classifier_name_acronym + "NB-";
				break;
			
			case 7:
				 RandomForest   cfs8 = new RandomForest();
				 CfsArray[j]         = cfs8;
				 classifier_name_acronym =  classifier_name_acronym + "RF-";
				break;
						
			case 8:
				 RBFNetwork     cfs9 = new RBFNetwork();
				 CfsArray[j]         = cfs9;
				 classifier_name_acronym =  classifier_name_acronym + "RBF-";
				break;
		}//switch
	}
	
	
	int len =  classifier_name_acronym.length();
	classifier_name_acronym = classifier_name_acronym.substring(0, len-1);
	return CfsArray;
	
}




//This program will take vote of 8 algorithms
private Evaluation cross_pred_max_voting(Classifier[] cfsArray) 
{

	  Vote voter =  new Vote();  
	  voter.setClassifiers(cfsArray); 
	  voter.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE, Vote.TAGS_RULES));
	  
	
 Evaluation evaluation = null;

try
{
		
    voter.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	System.out.println("h1");
	evaluation.evaluateModel(voter, tests);
  
	System.out.println("h2");

} catch (Exception e) 
{

	e.printStackTrace();
}

return evaluation;

}



//This is the main function
public static void main(String args[])
{	  	

	  cross_log_pred_max_voting_best_10_itr clps =  new cross_log_pred_max_voting_best_10_itr();
	
	  double precision[]   = new double[clps.iterations];
	  double recall[]      = new double[clps.iterations];
	  double accuracy[]    = new double[clps.iterations];
	  double fmeasure[]    = new double[clps.iterations];	
	  double roc_auc[]     = new double[clps.iterations];
	  
	  
	  //==================Run for number of classifiers===============================//
	  
	//  clps.learn_and_insert_max_voting(precision, recall, accuracy,fmeasure,roc_auc, 9); 
	  // clps.learn_and_insert_max_voting(precision, recall, accuracy,fmeasure,roc_auc, 8);
	//  clps.learn_and_insert_max_voting(precision, recall, accuracy,fmeasure,roc_auc,7);
	 // clps.learn_and_insert_max_voting(precision, recall, accuracy,fmeasure,roc_auc, 6);
	// clps.learn_and_insert_max_voting(precision, recall, accuracy,fmeasure,roc_auc, 5);
	// clps.learn_and_insert_max_voting(precision, recall, accuracy,fmeasure,roc_auc, 4);
//	 clps.learn_and_insert_max_voting(precision, recall, accuracy,fmeasure,roc_auc, 3);
		
 }//main		
	
}// classs




