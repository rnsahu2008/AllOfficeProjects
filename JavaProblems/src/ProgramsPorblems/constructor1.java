package ProgramsPorblems;

public class constructor1 extends constructor

{
	constructor1()
	{
		//super(5);
	//	this(1);
		System.out.println("im without child");
		
	}
	
	constructor1(int i)
	{
		
		System.out.println("im child");
		
	}
	
	public static void main(String[] args) {
		
		constructor1 as = new constructor1(5);
		
		
	}
	
	
}
