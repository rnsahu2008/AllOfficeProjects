package ProgramsPorblems;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SumSubsetFromArray {
	Integer[] test= {3,5,7,0};
int sum =7;
//int first=test[0];

public void sumArray()
{
	Map<Integer, Integer> map = new HashMap<Integer, Integer>();

for(int i=0;i<test.length ;i++)
{
	//Integer k = sum-test[i];

	//if(Arrays.asList(test).contains(k))
	if(map.containsKey(test[i]))
	
	{
		
		System.out.println(test[i] +", "+map.get(test[i]));
		
	}
	
	else
	{
		map.put(sum-test[i], test[i]);
	}
}

//System.out.println(map);


}

public static void main(String[] args) {
	SumSubsetFromArray sm = new SumSubsetFromArray();
	sm.sumArray();
}
}


