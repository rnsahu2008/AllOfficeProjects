package ProgramsPorblems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Removefomlist {
	public static void main(String[] args) 
		{ 
	List<String> listOfPhones = new ArrayList<String>(Arrays.asList(
"iPhone 6S", "iPhone 6", "iPhone 5", "Samsung Galaxy 4", "Lumia Nokia")); 
		
		System.out.println("list of phones: " + listOfPhones); 
		// Iterating and removing objects from list 
	
		// This is wrong way, will throw ConcurrentModificationException 
		for(String k :listOfPhones)
		{
		listOfPhones.remove(2);
		
		}System.out.println(Arrays.asList(listOfPhones));
		
		
					
		
		
		
		Map<String,String> myMap = new HashMap<String,String>();
		myMap.put("1", "1");
		myMap.put("2", "2");

		myMap.put("3", "3");

		Iterator<String> it1 = myMap.keySet().iterator();
		while(it1.hasNext()){
			String key = it1.next();
			System.out.println("Map Value:"+myMap.get(key));
			if(key.equals("2")){
			it1.remove();
				System.out.println("kk "+myMap.get(key));
				//myMap.put("1","4");
							}}
		
		System.out.println(myMap);
		
		}			}