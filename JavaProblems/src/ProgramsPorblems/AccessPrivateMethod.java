package ProgramsPorblems;

import java.lang.reflect.Method;

public class AccessPrivateMethod 
{
	  public static void main(String[] args) throws Exception 
	  {
		  Test d = new Test();
		        Method m = Test.class.getDeclaredMethod("foo");
		        //m.invoke(d);// throws java.lang.IllegalAccessException
		        m.setAccessible(true);// Abracadabra 
		        m.invoke(d);// now its OK
		    }


	    }
	
	
	class Test{
		
		private void foo(){
	        System.out.println("hello foo()");
		}
  	}

