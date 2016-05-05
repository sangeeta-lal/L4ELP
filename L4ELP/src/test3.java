

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
	  
	  
	  
	  
  }
}
