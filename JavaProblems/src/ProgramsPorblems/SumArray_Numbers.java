package ProgramsPorblems;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SumArray_Numbers {
	
	Integer[] test= {1,2,3,4,5,6,7,10};
int sum =7;
int first=test[0];

public void sumArray()
{
	Map<Integer, Integer> map = new HashMap<Integer, Integer>();

for(int i=0;i<test.length ;i++)
{
	Integer k = sum-test[i];
	boolean b = Arrays.asList(test).contains(k);
	
	if(Arrays.asList(test).contains(k))
	{
		if(!map.containsKey(sum-test[i]))
		{
		map.put(test[i], k);
		}
	}
	
}

System.out.println(map);	

}

public static void main(String[] args) {
	SumArray_Numbers sm = new SumArray_Numbers();
	sm.sumArray();
}
}
