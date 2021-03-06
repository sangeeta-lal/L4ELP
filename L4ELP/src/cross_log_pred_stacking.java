import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Stacking;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.ADTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;

// This file will be used to ensemble based prediction using stacking of algorithms
public class cross_log_pred_stacking
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
String result_table = "test_stacking";


int iterations=1;
String source_project="tomcat";
String target_project = "cloudstack";
//String target_project="hd";

//String source_project="cloudstack";
//String target_project = "tomcat";
//String target_project="hd";

//String source_project="hd";
//String target_project = "tomcat";
//String target_project="cloudstack";

String source_file_path = path+"L4ELP\\dataset\\"+source_project+"-arff\\catch\\complete\\"+source_project+"_catch_complete.arff";		
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


//This function is used to train and test a using a given classifier
public Evaluation cross_pred_stacking() 
{
	
	  Classifier[] cfsArray = new Classifier[4]; 
	  BayesNet cfs1= new BayesNet();
	  ADTree cfs2= new ADTree();
	  DecisionTable cfs3= new DecisionTable();
	  NaiveBayes cfs4  = new NaiveBayes();
	  
	  cfsArray[0]=cfs1;
	  cfsArray[1]=cfs2;
	  cfsArray[2]=cfs3;
	  cfsArray[3]=cfs4;

	  
	  //BayesNet cfsm=new BayesNet();
	  Logistic cfsm =  new Logistic();
	  
	  Stacking stack_model= new Stacking();
	  stack_model.setClassifiers(cfsArray);
	  stack_model.setMetaClassifier(cfsm);
	  stack_model.setSeed(1);
	 
	  System.out.println("here");
	
	
     Evaluation evaluation = null;

    try
    {
     
    		
        stack_model.buildClassifier(trains);
    	evaluation= new Evaluation(trains);	
    	evaluation.evaluateModel(stack_model, tests);


    } catch (Exception e) 
    {

	e.printStackTrace();
    }

return evaluation;


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


//This method computes the average value  and std. deviation and inserts them in a db
public void compute_avg_stdev_and_insert(String classifier_name, double[] precision, double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
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
	
	String insert_str =  " insert into "+ result_table +"  values("+ "'"+ source_project+"','"+ target_project+"','"+ classifier_name+"',"+ trains.numInstances() + ","+ tests.numInstances()+","
	                       + iterations+","+trains.numAttributes() +","+avg_precision+","+ std_precision+","+ avg_recall+","+ std_recall+","+avg_fmeasure+","+std_fmeasure+","+ avg_accuracy 
	                       +","+std_accuracy+","+ avg_roc_auc+","+ std_roc_auc+" )";
	System.out.println("Inserting="+ insert_str);
	
	conn = initdb(db_name);
	if(conn==null)
	{
		System.out.println(" Databasse connection is null");
		
	}
	
	try 
	{
		stmt = conn.createStatement();
		stmt.executeUpdate(insert_str);
	} catch (SQLException e) {
		
		e.printStackTrace();
	}

}

//This is the function created to store the files to help in debugging
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

	  cross_log_pred_stacking clps =  new cross_log_pred_stacking();
	
	  double precision[]   = new double[clps.iterations];
	  double recall[]      = new double[clps.iterations];
	  double accuracy[]    = new double[clps.iterations];
	  double fmeasure[]    = new double[clps.iterations];	
	  double roc_auc[]     = new double[clps.iterations];
		
		
		for(int i=0; i<clps.iterations; i++)
			 {
			    clps.read_file(i+1);
			   
				clps.pre_process_data();
				clps.result = clps.cross_pred_stacking();				
				
				precision[i]         =   clps.result.precision(1)*100;
				recall[i]            =   clps.result.recall(1)*100;
				accuracy[i]          =   clps.result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   clps.result.fMeasure(1)*100;
				roc_auc[i]           =   clps.result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());				
				
					
			}
				  
		   clps.compute_avg_stdev_and_insert("stacking", precision, recall, accuracy, fmeasure , roc_auc );
    
     }//main	
	
	
}// classs




