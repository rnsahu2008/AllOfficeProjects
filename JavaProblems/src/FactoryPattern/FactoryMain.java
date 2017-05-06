package FactoryPattern;

public class FactoryMain {
	
	public static void main(String[] args)
	{
		OperatingSystemFactory osf = new OperatingSystemFactory();
		//OS obj =osf.getInstance("Closed");
		OS obj =osf.getInstance("Open");
		//OS obj =osf.getInstance("Test");
		
		obj.spec();
		
	}
}

