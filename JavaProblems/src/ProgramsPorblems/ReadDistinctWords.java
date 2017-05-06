package ProgramsPorblems;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ReadDistinctWords {
	

	public void distinctword() throws IOException
	{
		
	
		FileReader fr = new FileReader(new File("D://Test.txt"));
		BufferedReader bar = new BufferedReader(fr);
		String strLine1;
		int count = 0;
		Set<String> st = new HashSet<String>();
		while ((strLine1 = bar.readLine()) != null) {
			// System.out.println(strLine1);

			String[] words = strLine1.split(" ");

			for (String k : words) {
				st.add(k);
			}
		}
		Iterator iter = st.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}

	}


	

	public void distinctwordByMap() throws IOException
	{
		
	
		FileReader fr = new FileReader(new File("D://Test.txt"));
		BufferedReader bar = new BufferedReader(fr);
		String strLine1;
		Map<String, Integer> map= new HashMap<String,Integer>();
		int count = 1;
		Set<String> st = new HashSet<String>();
		while ((strLine1 = bar.readLine()) != null) {
			// System.out.println(strLine1);

			String[] words = strLine1.split(" ");
			for (String k : words) {
				if(map.containsKey(k))
				{					
				map.put(k, count+1);	
					
				}
				else
				{
					map.put(k, count);
					
				}
			}
		}

		for(String str:map.keySet())
		{
			if(map.get(str)==1)
		System.out.println(str);
		}
	}
	


	

	public static void main(String[] args) throws IOException
	{
		ReadDistinctWords rd = new ReadDistinctWords();
		//rd.distinctword();
     rd.distinctwordByMap();
}
}