import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.meta.Stacking;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.ADTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;








public class cross_log_pred 
{

	
	// this is the direct prediction model for classification
	public void direct_pred() 
	{
 
       try
         {
		 // DataSource trainsource = new DataSource("F:\\Research\\L4ELP\\dataset\\tomcat-arff\\catch\\complete\\tomcat_catch_complete.arff");	
    	   DataSource trainsource = new DataSource("F:\\sms-train.arff");		
 		  
		  Instances trains = trainsource.getDataSet();
		  trains.setClassIndex(0);
		  
		  StringToWordVector filter = new StringToWordVector();
		  filter.setIDFTransform(true);
		  filter.setInputFormat(trains);
		  trains = Filter.useFilter(trains, filter);

		  
		// DataSource testsource = new DataSource("F:\\Research\\L4ELP\\dataset\\cloudstack-arff\\catch\\balance\\cloudstack_catch_balance_1.arff");	
		  DataSource testsource = new DataSource("F:\\sms-test.arff");		
	      Instances tests = testsource.getDataSet();
		  tests.setClassIndex(0);		  
		  tests = Filter.useFilter(tests, filter);
		  
		  
		  System.out.println("  Filter Train Data="+ trains.numInstances()+  "   test="+ tests.numInstances());
			
			/****  Print value of each attribute   ***/
			/*for (int i=0; i<tests.numAttributes(); i++)
			{
				
				 // Print the current attribute.
			    System.out.print(tests.attribute(i).name() + ": ");

			    // Print the values associated with the current attribute.
			    double[] values = tests.attributeToDoubleArray(i);
			    System.out.println(Arrays.toString(values));
			}*/
			
		  J48 tree = new J48(); 
		  tree.buildClassifier(trains);
		  
		  Evaluation eval = new Evaluation(trains);
		  eval.evaluateModel(tree, tests);
		  System.out.println(eval.toSummaryString("\n j48 Results\n======\n", false));
		  
		  
		  RandomForest rf = new RandomForest();
		  rf.buildClassifier(trains);
		  
		  eval = new Evaluation(trains);
		  eval.evaluateModel(rf, tests);
		  System.out.println(eval.toSummaryString("\n RF Results\n======\n", false));
		  
		  
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		
	}
	
	
	//this method will use ensmble based approach for prediction	
	public void use_ensemble() 
	{
		  
		try{	
			  double tp = 0;

			  double tn = 0;

			  double fp = 0;

			  double fn = 0;

			
			 String project = "F://result//";

			  String outFile = project + "ScoreAntStackBayes.txt";
			  String outFile2 = project + "FmeasureAntStackBayes.txt";
			  String outFile3 = project + "PredictedAntStackBayes.txt";

			  if (new File(outFile).exists()) {
			   new File(outFile).delete();
			  }
			  if (new File(outFile2).exists()) {
				   new File(outFile2).delete();
			  }
			  if (new File(outFile3).exists()) {
				   new File(outFile3).delete();
			 }
			  FileOutputStream out = new FileOutputStream(outFile, true);
			  FileOutputStream out2 = new FileOutputStream(outFile2, true);
			  FileOutputStream out3 = new FileOutputStream(outFile3, true);
			
		
		 DataSource trainsource = new DataSource("F:\\Research\\L4ELP\\dataset\\tomcat-arff\\catch\\complete\\tomcat_catch_complete.arff");	
   	   //DataSource trainsource = new DataSource("F:\\sms-train.arff");		
		  
		  Instances trains = trainsource.getDataSet();
		  trains.setClassIndex(0);
		  
		  StringToWordVector filter = new StringToWordVector();
		  filter.setIDFTransform(true);
		  filter.setInputFormat(trains);
		  trains = Filter.useFilter(trains, filter);

		  
		  DataSource testsource = new DataSource("F:\\Research\\L4ELP\\dataset\\cloudstack-arff\\catch\\balance\\cloudstack_catch_balance_1.arff");	
		//  DataSource testsource = new DataSource("F:\\sms-test.arff");		
	      Instances tests = testsource.getDataSet();
		  tests.setClassIndex(0);		  
		  tests = Filter.useFilter(tests, filter);
		  
		  
		  Classifier[] cfsArray = new Classifier[6]; 
		  Logistic cfs1= new Logistic();
		  BayesNet cfs2= new BayesNet();
		  RBFNetwork cfs3= new RBFNetwork();
		  MultilayerPerceptron cfs4= new MultilayerPerceptron();
		  ADTree cfs5= new ADTree();
		  DecisionTable cfs6= new DecisionTable();
		  
		  cfsArray[0]=cfs1;
		  cfsArray[1]=cfs2;
		  cfsArray[2]=cfs3;
		  cfsArray[3]=cfs4;
		  cfsArray[4]=cfs5;
		  cfsArray[5]=cfs6;
		 
		  BayesNet cfsm=new BayesNet();
		  
		  Stacking ensemble= new Stacking();
		  ensemble.setClassifiers(cfsArray);
		  ensemble.setMetaClassifier(cfsm);
		  ensemble.setSeed(1);
		  ensemble.buildClassifier(trains);
		  
		  
		  StringBuffer   sb1 = new StringBuffer();
		  sb1.append("scores" + "\n");
		  out.write(sb1.toString().getBytes("utf-8"));
		  
		  StringBuffer   sb4 = new StringBuffer();
		  sb4.append("predicted" + "\n");
		  out3.write(sb4.toString().getBytes("utf-8"));
		  
		  
		  
		  for (int j = 0; j < tests.numInstances(); j++) {
			     Instance curr = tests.instance(j);

			     double actual = curr.classValue();

			     double[] scores = ensemble.distributionForInstance(curr);

			     double predicted = 0;

			     if (scores[0] > scores[1]) {
			      predicted = 0;
			     } else {
			      predicted = 1;
			     }

			     StringBuffer   sb2 = new StringBuffer();
			     sb2.append(scores[1] + "\n");
			     System.out.println(scores[1] + "\t"  + actual + "\t"
			       + predicted);
			     out.write(sb2.toString().getBytes("utf-8"));
			     
			     StringBuffer   sb5 = new StringBuffer();
			     sb5.append(predicted + "\n");
			     out3.write(sb5.toString().getBytes("utf-8"));
			     
			     if (actual == 1) {
			      if (predicted == 1) {
			       tp++;
			      } else {
			       fn++;
			      }
			     }

			     else if (actual == 0) {
			      if (predicted == 0) {
			       tn++;
			      } else {
			       fp++;
			      }
			     }
			    }

			  long testend = System.currentTimeMillis();
			  
			  System.out.println("TP=== " + tp);
			  System.out.println("TN=== " + tn);
			  System.out.println("FP=== " + fp);
			  System.out.println("FN=== " + fn);
			 // System.out.println("traintime=== " + traintime);
			  //System.out.println("testtime=== " + testtime);
			  
			  double Fmeasure = 0;
			  if ((2 * tp + fp + fn) > 0) {
			   Fmeasure = 2 * tp / (2 * tp + fp + fn);
			  } else {
			   Fmeasure = 0;
			  }
			  out.close();
			  out3.close();

			  double pre = 0;

			  if ((tp + fp) > 0) {
			   pre = tp / (tp + fp);
			  } else {
			   pre = 0;
			  }

			  double rec = 0;

			  if ((tp + fn) > 0) {
			   rec = tp / (tp + fn);
			  } else {
			   rec = 0;
			  }
			  System.out.println("F-measure=== " + Fmeasure);
			  System.out.println("Precision=== " + pre);
			  System.out.println("Recall=== " + rec);
			  
			  StringBuffer   sb3 = new StringBuffer();
			  sb3.append("F-measure" + "\t" + Fmeasure + "\n" + "Precision" + "\t" + pre + "\n" + "Recall" + "\t" + rec + "\n"+ "TP" + "\t" + tp + "\n"+ "TN" + "\t" + tn + "\n"+ "FP" + "\t" + fp + "\n"+ "FN" + "\t" + fn + "\n" + "traintime" + "\t" +  "\n"+ "testtime" + "\t" + "\n");
			  out2.write(sb3.toString().getBytes("utf-8"));
			  out2.close();
		  
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[])
	{
		cross_log_pred obj = new cross_log_pred();
		//obj.direct_pred();
		
		obj.use_ensemble();
		
	}


	

	
}
