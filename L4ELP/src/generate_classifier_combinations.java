




//Java program to print all combination of size r in an array of size n
import java.io.*;

class  generate_classifier_combinations {
	
	//file path
	static String file_path = "F:\\Research\\L4ELP\\result\\comb";
	
	//static String file_path = "E:\\Sangeeta\\Research\\L4ELP\\result\\comb";

 static void combinationUtil(int arr[], int data[], int start,
                             int end, int index, int r, BufferedWriter bw)
 {
     // Current combination is ready to be printed, print it
     if (index == r)
     {
         for (int j=0; j<r; j++)
         { System.out.print(data[j]);
           
         try
           {
			bw.write(data[j]+"");
		   
           } catch (IOException e) 
            {
		
			  e.printStackTrace();
		    }
         
         }//for
         
         
         try
         {
			bw.write("\n");
		   
         } catch (IOException e) 
          {
		
			  e.printStackTrace();
          }//catch
         
         System.out.println("");
         return;
     }

     
     for (int i=start; i<=end && end-i+1 >= r-index; i++)
     {
         data[index] = arr[i];
         combinationUtil(arr, data, i+1, end, index+1, r, bw);
     }
 }

 
 static void printCombination(int arr[], int n, int r)
 {
	 
	 file_path =  file_path+"_"+r+".txt";
	 
	 BufferedWriter bw  = null;
	 try 
	 {
		bw = new BufferedWriter(new FileWriter(file_path,true));
		
	  } catch (IOException e)
	 {
	
		e.printStackTrace();
	 }
	 
     // A temporary array to store all combination one by one
     int data[]=new int[r];

     // Print all combination using temprary array 'data[]'
     combinationUtil(arr, data, 0, n-1, 0, r, bw);
    
     try 
     {
		bw.close();
	 } catch (IOException e)
     {
	
		e.printStackTrace();
	 }
     
 }

 /*Driver function to check for above function*/
 public static void main (String[] args) {
     int arr[] = {1, 2, 3, 4, 5,6,7,8,9};
     int r =9;
     int n = arr.length;
     printCombination(arr, n, r);
 }
}
