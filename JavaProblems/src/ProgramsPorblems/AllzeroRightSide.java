package ProgramsPorblems;

import java.util.Arrays;


public class AllzeroRightSide {
	static void swap1(int[] nums) {
		int m=0;int n=1;
		while(m <nums.length && n <nums.length){	
			if(nums[m]==0 && n <nums.length && nums[n]!=0){
				int temp=nums[m];
				nums[m]=nums[n];
				nums[n]=temp;
				m++;n++;
			}else {
				n++;
			}}
		System.out.println(Arrays.toString(nums));
	}
public static  void main(String[] args) 
{
	int[] nums={0,3,6,9,0,0,9};
	AllzeroRightSide obj = new AllzeroRightSide();
	obj.swap1(nums);
	
}
}
