package ProgramsPorblems;

//import TestInterface

public class OverrideExam extends testb{
	

	public void test(int a,int b)
	{
		
		System.out.println("testb class");
	}
	
	public void test(int a)
	{
		
		System.out.println("new class");
	}
	public static void main(String[] args) {
		
		testb tb = new testb();
		tb.test(5);
		
	}
	

	

}

class testb 
{
	
	public void test(int a)
	{
		System.out.println("ovverrid class");
		
	}

	}
