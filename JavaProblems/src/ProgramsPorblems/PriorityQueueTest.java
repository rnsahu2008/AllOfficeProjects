package ProgramsPorblems;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class PriorityQueueTest {
	
	public int findKthLargest(int[] nums, int k) {
	    PriorityQueue<Integer> q = new PriorityQueue<Integer>(k);
	    System.out.println(q.size());
	    for(int i: nums){
	        q.add(i);
//		int[] arr={2,8,3,12,13,4,5,6,1,14,17};	     
	        System.out.println(q);
	        if(q.size()>k){
	       	 
	            q.poll();
	            System.out.println(q);
	        }
	    }
	 
	    return q.peek();
	}
//Method 2
/*	public int findKthLargest1(int[] nums, int k) {
	    Arrays.sort(nums);
	    System.out.println(Arrays.toString(nums));
	    System.out.println(nums.length);
	    System.out.println(nums.length-k);
	
	    return nums[nums.length-k];
	}
*/
	public static void main(String[] args) {
		
		int[] arr={8,2,3,12,13,4,5,6,1,14,17};
		//ArrayList<Integer> ar = new  ArrayList<Integer>(Arrays.asList(arr));
		//PriorityQueue<Integer> pq= new PriorityQueue<Integer>(Arrays.asList(arr));
		PriorityQueueTest pq1 = new PriorityQueueTest();
		int k=pq1.findKthLargest(arr, 4);
		System.out.println(k);
		
		/*int l=pq1.findKthLargest1(arr, 1);
		System.out.println(l);
	*/	
	}

}
