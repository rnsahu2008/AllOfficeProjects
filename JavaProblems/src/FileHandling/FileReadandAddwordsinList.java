package FileHandling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.html.parser.Entity;


public class FileReadandAddwordsinList {
	
	public static void main(String[] args) throws IOException {
		List<String> words = new ArrayList<String>();

		Map<String, Integer> map = new HashMap<String, Integer>();
		String line;
		BufferedReader br = new BufferedReader(new FileReader("D:\\AAAAAAA\\File.txt"));
		while((line=br.readLine())!=null)
		{
					    //words.add(line);
			
			String[] str =line.split(" ");
			
			for (String st:str)
			{
				words.add(st);
				
			}
			
		}
		
		//System.out.println(words);
		
		for (String mp:words)
		{
			if(map.containsKey(mp))
			{
				
				map.put(mp, map.get(mp)+1);
			}
			else
			{
				map.put(mp, 1);
				
			}
		}
		
        Set<Entry<String, Integer>> set = map.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
        Collections.sort(list,new Comparator<Map.Entry<String, Integer>>() 
        		{

					public int compare(Map.Entry<String, Integer> o1,
							Map.Entry<String, Integer> o2) {
						// TODO Auto-generated method stub
						return o1.getValue().compareTo(o2.getValue());
					}
		});
        
        
        
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
     
        System.out.println(list.get(0));
       

      /*  for(Map.Entry<String, Integer> entry:list){
            System.out.println(entry.getKey()+"--"+entry.getValue());
        }*/
    }
		
		
   }


