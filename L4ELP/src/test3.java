

public class test3
{


  public static void main(String args[])
  {
	 
	  
	  double a []= new double[5];
	  a[0] = 20.5;
	  a[1]=  25.0;
	  a[2] = 26.0;
	  a[3]=  27.0;
	  a[4] = 29.0;
	  
	  util4_met ut = new util4_met();
	  double mean = ut.compute_mean(a);
	  double stddev =  ut.compute_stddev(a);
	  System.out.println(" S.O.P="+ mean+ " std="+ stddev);
	  
	  
	  mport numpy as np
	  >>> from sklearn import metrics
	  >>> y = np.array([1, 1, 2, 2])
	  >>> pred = np.array([0.1, 0.4, 0.35, 0.8])
	  >>> fpr, tpr, thresholds = metrics.roc_curve(y, pred, pos_label=2)
	  >>> metrics.auc(fpr, tpr)
	  0.75
	  http://stackoverflow.com/questions/15252468/calculate-auc-in-java
	  
		  TPR = TP/(TP + FP) = 90/(90+10) = 0.9
		  FPR = FP/(FP + TN) = 10/(10 + 999890) = 0.000010001
		  
		Looks good titorial:		  http://stackoverflow.com/questions/15226043/does-anyone-know-how-to-generate-auc-roc-area-based-on-the-predition
	  
  }
}
