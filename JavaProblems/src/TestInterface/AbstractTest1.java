package TestInterface;

import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;

class Test2 
{
	public int same(int k) {
		return 0;
	}

	
 }

public abstract class AbstractTest1 implements Interface1, Interface2 
{
//may or may not define interface methods
}

class ExtClsIntrfc extends Test2 implements Interface1, Interface2
{
	
	public void same() 
	{
		System.out.println("my interface i dont know");
	}
}

class Test3 implements interface3 {
	public void same() {

	}
	public int same(int k) {
		// TODO Auto-generated method stub
		return 0;
	}

}
