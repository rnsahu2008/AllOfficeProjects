package ProgramsPorblems;

public class SecondMeth2 {
	
	
	public static void main(String[] args) 
	{
	//int[] arr ={1000,1000,100,1000,1000,1000};

	int[] arr ={11,11,2,3,9,-100,-10,10,-23,4,9};
		
		int highest = arr[0];
		int secHighest = arr[1];
		boolean same = false;
		if(highest < secHighest) {
			secHighest =  arr[0];
			highest = arr[1];
		}
		if(highest == secHighest) {
			same = true;
		}
		
		for (int i = 2; i < arr.length; i++) {
			int element = arr[i];
			// case 1. element > highest			
			if(element > highest) {
				secHighest = highest;
				highest = element;				
			}
			// case 2. element = highest
			if(element == highest) {
			//	continue;
			}			
			// case 3. highest > element > secHighest
			if(element > secHighest && element < highest) {
				secHighest = element;			
			}			
			// case 4. element == secondHighest
			// if(element == secHighest) {
				// continue;
			//}			
			
			// case 5. element < secHighest
			if(element < secHighest) {
				if(same) {
					secHighest = element;
					same = false;
				}
				// do nothing
			}
			//
		}
		
		System.out.println("highest  "+highest);
		System.out.println("secHighest  "+secHighest);

	
		
		
		
	}

}
