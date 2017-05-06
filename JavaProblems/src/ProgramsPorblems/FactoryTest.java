package ProgramsPorblems;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;


	public class FactoryTest 
	{
	    private String param = "";
	 
	    public FactoryTest(String param) {
	        this.param = param;
	    }
	 
	    @BeforeClass
	    public void beforeClass() {
	        System.out.println("Before SimpleTest class executed.");
	    }
	 
	    @Test
	    public void testMethod() {
	        System.out.println("testMethod parameter value is: " + param);
	    }
	
	 
	    }
	

