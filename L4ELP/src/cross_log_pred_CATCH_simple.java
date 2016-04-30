import java.io.File;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.ADTree;
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
public class cross_log_pred_CATCH_simple
{

	//String path = "E:\\Sangeeta\\Research\\";
	String path = "F:\\Research\\";
	
	String source_project="tomcat";
	String target_project = "cloudstack";
	//String target_project="hadoop";
	
	//String source_project="cloudstack";
	//String target_project = "tomcat";
	//String target_project="hadoop";
	
	//String source_project="hadoop";
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
   
	
// This function uses dataset from the ARFF files
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
public void avg_10_db_metrics_and_insert(Classifier classifier, FastVector pred_10_db)
{
	 // computes following metrics:
	/* 1. TP
	 * 2. TN
	 * 3. Precision
	 * 4. Recall
	 * 5. Accuracy
	 * 6. F measure
	 * 7. ROC 
	 * */
	double correct = 0;
	for (int i = 0; i < pred_10_db.size(); i++)
	{
		NominalPrediction np = (NominalPrediction) pred_10_db.elementAt(i);
		if (np.predicted() == np.actual()) {
			correct++;
		}
	}

	double accuracy =  100 * correct / pred_10_db.size();
	System.out.println("Acc = "+ accuracy + "  size="+ pred_10_db.size());
	
	
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
			  					new MultilayerPerceptron(),
			  					new ADTree(),
			  					new DecisionTable()}; 
	 
		cross_log_pred_CATCH_simple clp = new cross_log_pred_CATCH_simple();
		
		// Length of models
		for(int j=0; j<models.length; j++)
		{
			FastVector pred_10_db = new FastVector();
			for(int i=0; i<1; i++)
				{
					clp.read_file(i+1);
					clp.pre_process_data();
					clp.result = clp.cross_pred(models[j]);
					pred_10_db.appendElements(clp.result.predictions());
					
				}
			clp.avg_10_db_metrics_and_insert(models[j], pred_10_db);
		}		
		
		
	}

	
}
