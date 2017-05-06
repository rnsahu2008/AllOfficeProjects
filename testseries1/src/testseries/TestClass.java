package testseries;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestClass  {
 	
	@Test
 public void TestSeries1()
 { 
	Series s = new Series();
	float sum= s.series(5);
	System.out.println(sum);
	//(expected, actual);
	Assert.assertEquals(2.2833335f,sum);
		      
 }
 
@Test
	public void TestSeries2()
	 { 
		Series s = new Series();
		float sum= s.series(-4);
		System.out.println(sum);
		//(expected, actual);
		Assert.assertEquals(2.2833335f,sum);
			      
	 }
@Test
public void TestSeries3()
	 { 
		Series s = new Series();
		float sum= s.series(0);
		System.out.println(sum);
		//(expected, actual);
		Assert.assertEquals(2.2833335f,sum);
			      
	 }
@Test	 
public void TestSeries4()
	 { 
		Series s = new Series();
		float sum= s.series(2);
		System.out.println(sum);
		//(expected, actual);
		Assert.assertEquals(1.5f,sum);
			      
	 }

}


