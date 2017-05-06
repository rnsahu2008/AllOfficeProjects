package ProgramsPorblems;
import java.util.HashSet;
import java.util.Set;
public class AllAnagrams {
	
	
	public static Set<String> generatePerm(String input)
	{
	    Set<String> set = new HashSet<String>();
	    if (input == "")
	        return set;

	    Character a = input.charAt(0);

	    if (input.length() > 1)
	    {
	        input = input.substring(1);
	        System.out.println("Tes"+input);

	        Set<String> permSet = generatePerm(input);

	        for (String x : permSet)
	        {
	            for (int i = 0; i <= x.length(); i++)
	            {
	                set.add(x.substring(0, i) + a + x.substring(i));
	            }
	        }
	    }
	    else
	    {
	        set.add(a + "");
	    }
	    return set;
	}

	
    public static void main(String args[])throws Exception
    {
        AllAnagrams ob=new AllAnagrams();
        System.out.println(ob.generatePerm("ram"));
        
    }
}

