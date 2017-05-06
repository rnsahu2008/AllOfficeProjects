package comperator;

public class AllAboutStaic 
{		static int count=0;
int k =1100;

int i=0;
	AllAboutStaic()
	{

		count++;
		
	}

	public static void m1()
	{
System.out.println("Stat");
AllAboutStaic sc = new AllAboutStaic();
System.out.println(sc.i);
		
	}
	public  void m2()
	{
System.out.println("nonstatic");
		
	}
	public static void main(String[] args) {
		
		AllAboutStaic sc = new AllAboutStaic();
		AllAboutStaic sc2 = new AllAboutStaic();
		
		AllAboutStaic sc1 = new AllAboutStaic();
		System.out.println(count);
		System.out.println(sc.count);
		m1();
		sc.m2();
	}
	
}
