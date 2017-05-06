package ProgramsPorblems;

public class factorialByRecursion {

	static int result=1;
	public static int fibo(int k)
	
	{
		if(k==1)
			return 1;
		else
		return k*fibo(k-1);		
	
	}
	public static void main(String[] args) {
		
		
		System.out.println(factorialByRecursion.fibo(3));
	
	}

}
