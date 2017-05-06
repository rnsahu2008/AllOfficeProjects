package Akamai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Arraytest {
	
/*	public void SortArray(int[] arr)
	{
		
	int k = arr.length;
	for(int i =0;i<k;i++)
	{
		for (int j=i+1;j<arr.length;j++)
			
		{
			if(arr[j]<arr[i])
				
			{
				int temp=arr[j];
				arr[j]=arr[i];
				arr[i]=temp;
				
			}
			
		}
		
	}	
	
		System.out.println(Arrays.toString(arr));
		
	}
*/	
	
	public void sortArray(int[] arr1)
	{
		int count0=0;
		int count1=0;
		int count2=0;
		Vector<Integer> v = new Vector<Integer>();
		ArrayList<Integer> ls = new  ArrayList<Integer>();
		HashMap<Integer, String> hs = new HashMap<Integer, String>();
		System.out.println(hs.size());
		HashMap<Integer ,Integer> map = new HashMap<Integer, Integer>();
		for (int i=0;i<arr1.length;i++)
			
		{
			if(arr1[i]==0)
			{
				map.put(arr1[i], count0+1);
				count0++;
				
			}
			else if(arr1[i]==1)
				
			{
				map.put(arr1[i], count1+1);
				count1++;
					
			}	
			
			else if(arr1[i]==2)
				
			{
				map.put(arr1[i], count2+1);
				count2++;
					
			}	
			
			
			
		}
		
	//	System.out.println(map);
		
		for (int i:map.keySet())
		{
			if (i==0)
			{ int x=0;
				while(x<map.get(i))
					x++;
					ls.add(0);
				
					
				}
				
			
				
			else if (i==1)
			{
				int y=0;
				while(y<map.get(i))
					y++;

					ls.add(1);
				
			}
			else 	if (i==2)
			{
				int z=0;
				while(z<map.get(i))
					z++;

					ls.add(2);
				
					
				}
				
			}
		System.out.println(ls);
				
		}
		
		
	
	
	
	
	
	public static void main(String[] args) {
		
		int[] arr1= {0,1,2,1,1,0,2};
			Arraytest arr = new Arraytest();
			/*	arr.SortArray(arr1);
	*/	
		
			arr.sortArray(arr1);
		

		
		
	}

}
