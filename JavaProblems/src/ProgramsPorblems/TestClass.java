package ProgramsPorblems;

//import for Scanner and other utility  classes
import java.util.*;



class TestClass {
  
  public void getSubstringNumber1(String main,String sub)
  {
      String[] substr = main.split(" ");
      
      Map<String, Integer> map = new HashMap<String, Integer>();
		
      int counter=1;
      for (String subs:substr)
      {
			if(map.containsKey(subs))
			{
				map.put(subs, counter+1);
				
			}
			
			else
			{
				map.put(subs, counter);
				
			}
		}    
      
      if(map.containsKey(sub))
      {
          System.out.println("Yes:"+map.get(sub));
          
      }
      
      else
      {
                   System.out.println("no");
      }
  }

  
  
  public void getSubstringNumber(String main,String sub)
  {
   // String str = "helloslkhellodjladfjhello";
  //String findStr = "hello";
  int lastIndex = 0;
  int count = 0;

  while ((lastIndex = main.indexOf(sub, lastIndex)) != -1) {
      count++;
      lastIndex += sub.length() - 1;
  }

  System.out.println(count);
   
  }

  public static void main(String args[] ) throws Exception {
      
      TestClass obj=new TestClass();
      Scanner scn = new Scanner(System.in);
      String main = scn.nextLine();
       Scanner scn1 = new Scanner(System.in);
      String sub = scn1.nextLine();
      
      obj.getSubstringNumber1(main,sub);

  }
}
