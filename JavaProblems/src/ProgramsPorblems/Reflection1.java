package ProgramsPorblems;

public class Reflection1  {
	
private String idcode ="100";

private String test1;
private String name;
	
	private String getprivate()
	{
		return "Test rflection1";
		
	}
	
	private String getOtherprivate(int thisint,String thatstring)
	{
		return "Test rflection1" + thisint +" "+ thatstring;
		
	}

	public void WihoutargumentsMethod()
	{
		System.out.println("public method printed");
	}

	public String WithargumentsMethod(String str1,int int1)
	{
		String str2="";
		System.out.println("public method printed" +str2);
		
		return str2;
	}
	// Recu Recu1;
	
	public void setname(String newName)
	{
		
		name=newName;
		
	}
	
	
	public String getname()
	{
		return name;
		
	}
	
	
	public Reflection1()
	{System.out.println("i am constructor");
	}
	
	
	public  Reflection1(int number1,String string1)
	{
		System.out.println( "rflection1 class 2 Argument " + number1 +" and "+ string1);
		
	}
	
	protected static class ram
	{
		public void tets()
		{
			System.out.println("Test");
			
		}
	}

	interface interface1
	{
		public void int1();

		public void int2();

		
	}
	interface interface2
	{
		public void int3();

		public void int4();

		
	}
	
}

	


