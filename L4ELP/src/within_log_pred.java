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
 * */
public class within_log_pred
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
	 

	int iterations=10;	
	String type = "catch";
	//String type ="if";

	String source_project="tomcat";	
	//String source_project="cloudstack";	
	//String source_project="hd";
	
	
	String db_name ="logging4_elp";
	String result_table = "within_log_pred_"+type;

	
	// we are using balanced files for with-in project logging prediction		
   	String source_file_path = path+"L4ELP\\dataset\\"+source_project+"-arff\\"+type+"\\balance\\"+source_project+"_"+type+"_balance";
		
	//DataSource trainsource;
	DataSource all_data_source;
	Instances all_data;
			
	Instances trains;
	Instances tests;
	Evaluation result;
		
	int instance_count_source = 0;
	 

	//Connection conn=null;	
	//java.sql.Statement stmt = null;
   
	
	
	// This function uses dataset from the ARFF files
	public void read_file(int i)
	 { 
		try 
			{
			
				all_data_source = new DataSource(source_file_path+"_"+i+".arff");
				all_data = all_data_source.getDataSet();
				all_data.setClassIndex(0);	
				
				instance_count_source = all_data.numInstances();
				
				
				System.out.println("Instance count source ="+ instance_count_source);// + "  Instance count target="+ instance_count_target);
		    
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
			  tfidf_filter.setInputFormat(all_data);
	     	  all_data = Filter.useFilter(all_data, tfidf_filter);     	  

	 	     //2. Standarize  (not normalize because normalization is affected by outliers very easily)   	  
	     	  Standardize  std_filter =  new Standardize();
	     	  std_filter.setInputFormat(all_data);
	     	  all_data= Filter.useFilter(all_data,std_filter);     	  
	     	
	 	     //3. Discretizations
	     	  Discretize dfilter = new Discretize();
		      dfilter.setInputFormat(all_data);
		      all_data = Filter.useFilter(all_data, dfilter);
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

}
	

//This method will divide the data in =to two parts: Train and Test
public void create_train_and_test_split(double train_size, double test_size) 
{
	all_data.randomize(new java.util.Random(0));
	int trainSize = (int) Math.round(all_data.numInstances() * train_size);
	int testSize = all_data.numInstances() - trainSize;
	trains = new Instances(all_data, 0, trainSize);
	tests = new Instances(all_data, trainSize, testSize);

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


public Connection initdb(String db_name)
{
	Connection conn= null;
	
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


// This method computes the average value  and std. deviation and inserts them in a db
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
		
		String insert_str =  " insert into "+ result_table +"  values("+ "'"+ source_project+"','"+ "same_as_source" +"','"+ classifier_name+"',"+ trains.numInstances() + ","+ tests.numInstances()+","
		                       + iterations+","+trains.numAttributes() +","+avg_precision+","+ std_precision+","+ avg_recall+","+ std_recall+","+avg_fmeasure+","+std_fmeasure+","+ avg_accuracy 
		                       +","+std_accuracy+","+ avg_roc_auc+","+ std_roc_auc+" )";
		System.out.println("Inserting="+ insert_str);
		
		Connection conn2 = initdb(db_name);
		if(conn2==null)
		{
			System.out.println(" Databasse connection is null");
			
		}
		
		try 
		{
			Statement stmt2 = conn2.createStatement();
			stmt2.executeUpdate(insert_str);
			stmt2.close();
			conn2.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	
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
	 
		within_log_pred clp = new within_log_pred();
		
		
		// Length of models
		for(int j=0; j<models.length; j++)
		{
			
			String classifier_name =  models[j].getClass().getSimpleName();
			
			double precision[]   = new double[clp.iterations];
			double recall[]      = new double[clp.iterations];
			double accuracy[]    = new double[clp.iterations];
			double fmeasure[]    = new double[clp.iterations];	
			double roc_auc[]     = new double[clp.iterations];
			
			
			for(int i=0; i<clp.iterations; i++)
				 {
				    clp.read_file(i+1);
				   
					clp.pre_process_data();
					clp.create_train_and_test_split(0.7,0.3);
					
					clp.result = clp.cross_pred(models[j]);				
					
					precision[i]         =   clp.result.precision(1)*100;
					recall[i]            =   clp.result.recall(1)*100;
					accuracy[i]          =   clp.result.pctCorrect(); //not required to multiply by 100, it is already in percentage
					fmeasure[i]          =   clp.result.fMeasure(1)*100;
					roc_auc[i]           =   clp.result.areaUnderROC(1)*100;
					
					
					 // Alternative way to compute all the metrics mannually
				   /* util4_met ut_obj=  new util4_met();
					FastVector pred_1_db = clp.result.predictions();
					precision[i]         = ut_obj.compute_precision(pred_1_db);
					recall[i]            = ut_obj.compute_recall(pred_1_db);
					accuracy[i]          = ut_obj.compute_accuracy(pred_1_db);
					fmeasure[i]          = ut_obj.compute_fmeasure(pred_1_db);
					roc_auc[i]           = ut_obj.compute_roc_auc(pred_1_db);//*/
			
					
					//@ Un comment to see the evalauation results
					//System.out.println(clp.result.toSummaryString());				
					
						
				}
					  
			   clp.compute_avg_stdev_and_insert(classifier_name, precision, recall, accuracy, fmeasure , roc_auc );
		}		
		
		
	}

	
}


