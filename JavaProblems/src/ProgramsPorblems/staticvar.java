package ProgramsPorblems;

public  class staticvar {
	
	public static int a = 5;
	public int b = 10;
	
	
public static void teststatic()

{
	final int k =6;
	
}
	
public static void main(String[] args)

{

	staticvar t1 = new staticvar(); 
	staticvar t2 = new staticvar();
	

	t1.a = 1;
	t2.a=7;
	t1.b=5;
	System.out.println(t1.a);
	System.out.println(t2.a);
	System.out.println(t1.b);
	System.out.println(t2.b);
	
	
	   String s="Sachin Tendulkar";  
	   System.out.println(s.substring(6));//Tendulkar  
	   System.out.println(s.substring(1,4));//Sachin  

}
}
