package ProgramsPorblems;

public class BaseClassDerivedClassObjects
{
		
	public static void main(String[] args) 
	{
		//test1 t = new test1();  	t.Area();
		test10 t1 = new test10();		t1.Area();
		test20 t2 = new test20();		t2.Area();
		test10 t3 = new test20();		t3.Area();
	//	test2 t3 = new test1();		t3.Area();//can go down to up ..only top to bottom allowed	
		
	}
}
 class test10{		
	 public  static void Area()	
	 {
		 System.out.println("test1");}}
 class test20 extends test10
{
	public static  void Area()
	{
		System.out.println("test2");	
	}
	}