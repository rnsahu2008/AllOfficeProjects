package testseries;

public class Series 
{

	public float series(int n)  
	{
		float sum=0;
	
		try 
		{
		
	if(n>0)
	{	
		for(int i=1;i<=n;i++)
		{		sum=sum+(float)1/i;		}
		
	}
	else{	System.out.println("invalid input");}
	
	return sum;
		}
			catch (ArithmeticException e) {
				 
			      System.out.println("Division by zero not Possible!");
			 
			    }
		return sum;
		}


	public static void main(String[] args) 
	{
		Series s = new Series();
		float k=s.series(5/0);
		System.out.println(k);
	}

}
