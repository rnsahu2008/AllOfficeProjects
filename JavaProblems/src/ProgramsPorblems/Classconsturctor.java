package ProgramsPorblems;

abstract class TestAbstract
{
    private int superVal;
    TestAbstract(int k)
	{
		this.superVal = k;
		
	}
	
   public void syso() {
	   System.out.println("Test my value: " + superVal);
	
    }

}


public class Classconsturctor extends TestAbstract
{
	Classconsturctor(int k) 
	{
		super(k);
	}

   public static void main(String[] args) {
	
	   Classconsturctor test = new Classconsturctor(10);
	test.syso();
   }	

}

