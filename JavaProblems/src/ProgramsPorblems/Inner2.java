package ProgramsPorblems;


public class Inner2 
{
	
	
public static void main(String[] args) {

	Inner1 obj = new Inner1();
	obj.s1=3;
	
	Inner1.B b1= obj.new B();
	b1.b=4;
	
	Inner1.C c1 = new Inner1.C();
	
	c1.c=4;
	c1.d=5;
}
}