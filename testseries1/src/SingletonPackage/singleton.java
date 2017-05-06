package SingletonPackage;
public class singleton 
{
private static singleton objinstance = new singleton();
 private singleton()
 {
	 
 }
  public static singleton getInstance(){
      return objinstance;
		   }
		   public void DisplayMessage(){
		      System.out.println("working Singleton class");
		   }
		}
		
