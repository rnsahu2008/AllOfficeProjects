package PolyMor;


public class class2 extends class1
{
	
	public void test1(int a,int b)
	{
			System.out.println("class1");	
	}
	public static void main(String[] args) 
	{
		class2 a = new class2();
		class1 b = new class1();
		class1 c = new class2();
	a.test1(4);
	b.test1(4);
	c.test1(4);
		
		
	}

}
