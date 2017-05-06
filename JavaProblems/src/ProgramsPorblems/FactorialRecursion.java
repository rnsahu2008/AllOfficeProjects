package ProgramsPorblems;

import java.util.Scanner;

public class FactorialRecursion {
	
	public int fact(int a)
	{
		if(a==1)
		{
			return 1;
		}
			
		return a*fact(a-1);
	}
	

	public static void main(String[] args) 
	{
		System.out.println("Test");
		Scanner scan = new Scanner(System.in);
		int a = scan.nextInt();
		System.out.println(a);
		FactorialRecursion rc = new FactorialRecursion();
		int k =rc.fact(a);
		System.out.println(k);
		
			
	}

}
