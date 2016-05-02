import java.io.BufferedReader;
import java.io.File;
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
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.ADTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;



/*
 * @Author: Sangeeta
 * 1. This is the simple log prediction code that is used to predict cross project log prediction using simple LogOpt method
 * 2. In this code we use  "TWO SOURCE PROJECTS" for training
 * */
public class cross_two_source_proj_log_pred_CATCH_simple
{

	/*
	 String path = "E:\\Sangeeta\\Research\\";
	 String user_name =  "sangeetal";
	 String password = "sangeetal";
	 String url = "jdbc:mysql://localhost:3307/";
	 String driver = "com.mysql.jdbc.Driver"; 
	  
	// */
	
	///*
	String path = "F:\\Research\\";
	String user_name =  "root";
	String password = "1234";
	String url = "jdbc:mysql://localhost:3306/";
	String driver = "com.mysql.jdbc.Driver";
	//*/
	 String db_name ="logging4_elp";
	 String result_table = "cross_two_source_proj_log_pred_catch_simple";
	
	
	String source_project="tomcat_cloudstack";
	String target_project="hd";
	
	//String source_project="cloudstack_hd";
	//String target_project = "tomcat";

	
	//String source_project="tomcat_hd";
	//String target_project="cloudstack";
	
	String source_file_path = path+"L4ELP\\dataset\\two-project-arff\\catch\\"+source_project+"_catch_complete.arff";		
	String target_file_path = path+"L4ELP\\dataset\\"+target_project+"-arff\\catch\\balance\\"+target_project+"_catch_balance";
	
	DataSource trainsource;
	DataSource testsource;
	Instances trains;
	Instances tests;
	Evaluation result;
	
	int instance_count_source = 0;
	int instance_count_target =0;
	Connection conn=null;	
    java.sql.Statement stmt = null;
   
	
// This function uses dataset from the ARFF files
public void read_file(int i)
 { 
	try 
		{
		
		System.out.println(source_file_path);
			trainsource = new DataSource(source_file_path);
			trains = trainsource.getDataSet();
			trains.setClassIndex(0);
			
			testsource = new DataSource(target_file_path+"_"+i+".arff");
			tests = testsource.getDataSet();
			
			tests.setClassIndex(0);
			
			instance_count_source = trains.numInstances();
			instance_count_target = tests.numInstances();
			
			System.out.println("Instance count source ="+ instance_count_source + "  Instance count target="+ instance_count_target);
	    
		} catch (Exception e) 
		{
		
			e.printStackTrace();
		}	  
		
	}
	

// This function is used to pre-process the dataset
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
	


// This function is used to train and test a using a given classifier
public Evaluation cross_pred(Classifier model) 
{
	Evaluation evaluation = null;
	
	try {
	      
		evaluation= new Evaluation(trains);		
		model.buildClassifier(trains);
		evaluation.evaluateModel(model, tests);
	
	
	} catch (Exception e) {
	
		e.printStackTrace();
	}

	return evaluation;
	
	//http://www.programcreek.com/2013/01/a-simple-machine-learning-example-in-java/
}

// This method computes the results of classifier
public void avg_10_db_metrics_and_insert(String classifier_name, FastVector pred_10_db, Connection conn)
{
	 // computes following metrics:
	/*
	 * 1. Precision
	 * 2. Recall
	 * 3. Accuracy
	 * 4. F measure
	 * 5. ROC 
	 * */

	double avg_precision = 0.0;
	double avg_recall = 0.0;
	double avg_accuracy = 0.0;
	double avg_fmeasure = 0.0;	
	double roc = 0.0;
	double total_instances = 0.0;
	
	util4_met  ut = new util4_met();
	
	avg_precision = ut.compute_precision(pred_10_db);
	avg_recall = ut.compute_recall(pred_10_db);
	avg_fmeasure = ut.compute_fmeasure(pred_10_db);
	avg_accuracy =  ut.compute_accuracy(pred_10_db);
	
	// Round all the values to two decimal places
		avg_precision =  Math.round(avg_precision * 100.0) / 100.0;
		avg_recall =     Math.round(avg_recall * 100.0) / 100.0;
		avg_fmeasure =   Math.round(avg_fmeasure * 100.0) / 100.0;
		avg_accuracy =   Math.round(avg_accuracy * 100.0) / 100.0;
		
    System.out.println("model ="+classifier_name +"   Acc = "+ avg_accuracy + "  size="+ pred_10_db.size());
	
	String insert_str =  " insert into "+ result_table +"  values("+ "'"+ source_project+"','"+ target_project+"','"+ classifier_name+"',"+ trains.numInstances() + ","+ tests.numInstances()+","
	                       + 10+","+avg_precision+","+ avg_recall+","+avg_fmeasure+","+ avg_accuracy +" )";
	
	
	try 
	{
		stmt = conn.createStatement();
		stmt.executeUpdate(insert_str);
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	
}

public Connection initdb(String db_name)
{
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

// This is the function created to store the files to help in debugging
  public void save_file_temp_location(Instances trains2, Instances tests2)
  	{
	 
	  try
	  {
	       ArffSaver saver = new ArffSaver();
           saver.setInstances(trains);
       
		   saver.setFile(new File("F:\\result\\tom_idf.arff"));
		
		   saver.writeBatch();
	
	 } catch (IOException e) 
	{
	
		e.printStackTrace();
	}
       
}


//This is the main function
public static void main(String args[])
	{
	
	  
	  Classifier models [] = {  new Logistic(),
			  					new BayesNet(),
			  					new RBFNetwork(),			  					
			  					new ADTree(),
			  					new DecisionTable(),
			  					new AdaBoostM1(),
			  					new J48(),  //Decision Tree
			  					new RandomForest(),
			  					new NaiveBayes()};
			  					//new MultilayerPerceptron()}; //removed because of high computational requirement
	 
		cross_two_source_proj_log_pred_CATCH_simple clp = new cross_two_source_proj_log_pred_CATCH_simple();
		clp.conn = clp.initdb(clp.db_name);

		if(clp.conn==null)
		{
			System.out.println(" Databasse connection is null");
			
		}
		
		// Length of models
		for(int j=0; j<models.length; j++)
		{
			FastVector pred_10_db = new FastVector();
			String classifier_name =  models[j].getClass().getSimpleName();
			for(int i=0; i<10; i++)
				{
					clp.read_file(i+1);
					clp.pre_process_data();
					clp.result = clp.cross_pred(models[j]);
					pred_10_db.appendElements(clp.result.predictions());
					
					//@ Un comment to see the evalauation results
					//System.out.println(clp.result.toSummaryString());	
				
				}
		
			
			clp.avg_10_db_metrics_and_insert(classifier_name, pred_10_db, clp.conn);
		}		
		
		
	}

	
}
