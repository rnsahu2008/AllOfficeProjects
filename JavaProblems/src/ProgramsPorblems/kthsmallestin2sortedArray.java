package ProgramsPorblems;

public class kthsmallestin2sortedArray {
	
	public int kthsmall(int[] arr1,int[] arr2,int k )
	{
		/*if (arr1.length == 0)
		{
	        return arr2[k];
		}
		else if (arr2.length == 0)
		{
	        return arr1[k];
		}
	    */
	int lenght1=arr1.length;
	int lenght2=arr2.length;
	 //  int mida1 = arr1.length/2;
	  // int mida2 = arr2.length/2;
	   
	   if(lenght1>lenght2)
	   {
		   return kthsmall(arr2, arr1, k);
		   
	   }
		if(lenght1==0)
		{
		        return arr2[k-1];
		}
		if(lenght2==0)
		{
		        return arr1[k-1];
		}	
		
		if(k==1)
			
		{
			return Math.min(arr1[0],arr2[0]);
			
		}
		int i=lenght1/2;
		int j=lenght2/2;
		
		
		if(arr1[i-1]<arr2[j-1])
			
		{
			
			
		}
		return j;
	}
	
 
public static void main(String[] args) {
	

	    int a[] = {10,20,30,50,60};
	    int b[] = {70,80,90,100,110,120};
	    
	    

	}

}
