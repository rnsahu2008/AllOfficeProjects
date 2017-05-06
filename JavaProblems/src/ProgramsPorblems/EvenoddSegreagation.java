package ProgramsPorblems;

import java.util.Arrays;

public class EvenoddSegreagation {
	
	
	public static void main(String[] args) {
		 int []array = {3,1,0,1,6,8,5,2,3};
		    evenOddFunction(array);
		    System.out.println(Arrays.toString(array));
		}

		public static void evenOddFunction(int[] data) 
		{
			int left=0;
			int right=data.length-1;
			
			while(left<=right)
			{
				if (data[left]%2==1 && data[right]%2==0)
				{
				int temp=data[left];
				data[left]=data[right];
				data[right]=temp;
					
					
				}
				
				else
				{
					if(data[left]%2==0)
						
					{
						left++;
						
					}
					if(data[right] % 2==1)
						
					{
						right--;
						
					}
					
				}
				
				
			}
			
		  		        }
}

