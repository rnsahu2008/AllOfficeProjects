package ProgramsPorblems;

public class SecHighest1 {
	
	public static void main(String[] args) {
		
//int[] arr ={-2,-3,-6,-12,-10,-10,-12,-13,0};
int[] arr ={-2,-2};
		
		int highest = arr[0];
		int secHighest = arr[1];
		
		if(highest < secHighest) {
			secHighest =  arr[0];
			highest = arr[1];
		}
		
	for(int i =2;i<arr.length;i++)
	{
		
		if(arr[i]>highest)
		{
			secHighest=highest;
			highest=arr[i];
			
		}
		else if(arr[i]==highest)
		{
			continue;
				
		}
		else if(arr[i]<highest && arr[i]>secHighest)
		{
			secHighest=arr[i];
			
		}
	}
		
	System.out.println(highest);
	System.out.println(secHighest);
		
	}}