package SortingandMerging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class SortingHashMapByKey {
	
	public static void main(String[] args) {
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("k13", 11);
		map.put("k9", 2);
		map.put("k8", 5);
		map.put("k2", 3);
		map.put("k1", 17);
		map.put("k7", 16);
		
	

	List<String> list = new ArrayList<String>();
	for(String str :map.keySet())
	{
		list.add(str);
	}
	Collections.sort(list);
	
	System.out.println(map.entrySet());
	
	}
}