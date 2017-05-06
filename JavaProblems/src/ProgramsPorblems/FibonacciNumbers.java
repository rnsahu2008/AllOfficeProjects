package ProgramsPorblems;

public class FibonacciNumbers {
	
	
	public int fibo(int i)
	{
		if(i==1 || i==2)
		{
		return 1;	
			
		}
		else
		{
			return fibo(i-1)+fibo(i-2);
			
		}
		
		
		
	}
	public static void main(String[] args) {
		
		FibonacciNumbers fb = new FibonacciNumbers();
		//fb.fibo(6);
		System.out.println(fb.fibo(19));
		
		
	}

}
