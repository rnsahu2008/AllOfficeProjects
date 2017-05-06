package SingletonDesign;

public class Sigleton 
{
public static void main(String[] args) {
	
	Abc obj = Abc.getInstance(); 
	Abc obj1 = Abc.getInstance(); //both are same object
	
	System.out.println(obj1+" "+obj);
}
	
}
