package SortingandMerging;

	import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.codehaus.jackson.map.util.Comparators;
	 
	public class SortmapbyValue {
	 
	    public static void main(String a[]){
	        Map<String, String> map = new HashMap<String, String>();
	        map.put("w", "y");
	        map.put("z", "z");
	        map.put("b", "a");
	        map.put("a", "x");
	        map.put("b", "f");
	        map.put("n", "p");
	  
	        Map<String, Integer> map1 = new HashMap<String, Integer>();
	        map1.put("w", 11);
	        map1.put("z", 21);
	        map1.put("b", 1);
	        map1.put("a", 1);
	        map1.put("m", 7);
	        map1.put("n", 1);
	  
List<Entry<String,Integer>> list2 = new ArrayList<Entry<String,Integer>>(map1.entrySet());
Collections.sort(list2,new Comparator<Entry<String,Integer>>() 
		{
	public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2)
	{
		
		return o1.getValue().compareTo(o2.getValue());
	}
});

	    
System.out.println(Arrays.asList(list2));

for(Entry<String,Integer> entr:list2)
{
	if(entr.getValue()==1)
	System.out.println(entr.getKey()+"====="+entr.getValue());

}

	    }
	}