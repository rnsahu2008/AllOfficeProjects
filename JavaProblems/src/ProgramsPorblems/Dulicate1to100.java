package ProgramsPorblems;

public class Dulicate1to100 {
	
	static int n=10;
	int sum=0;
	
	public int sumall(int n)
	
	{
		return  n*(n+1)/2;
		
	}
public int sum(int[] k)
	
	{
for(int i:k)
{
	sum=sum+i;
}
return sum;
		
	}
	
	public static void main(String[] args) {
		
		Dulicate1to100 d = new  Dulicate1to100();
		int[] k={1,2,3,4,5,6,6,7,8,9,10};
		
		int b=d.sumall(n);
		int c=d.sum(k);
		
		int e=c-b;
		System.out.println(e);
		
		
	}

	
}
