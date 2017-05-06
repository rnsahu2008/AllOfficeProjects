package ProgramsPorblems;
import java.util.HashMap;
import java.util.Scanner;

class maxminarray {
      
    
     public static int getMaxArrayvlaue(int[] inputArray)
     { 
    int maxValueArray = inputArray[0]; 
    for(int i=1;i < inputArray.length;i++)
    { 
      if(inputArray[i] > maxValueArray){ 
         maxValueArray = inputArray[i]; 
      } 
    } 
    return maxValueArray; 
  }

     public static int getMinimumArray(int[] inputArray)
     { 
    int minValueArray = inputArray[0]; 
    
    for(int i=1;i<inputArray.length;i++){ 
      if(inputArray[i] < minValueArray) { 
        minValueArray = inputArray[i]; 
      } 
    } 
    return minValueArray; 
  } 

    
    public static void main(String args[] ) throws Exception 
    {
            int[] array1 = new int[]{10, 11, 88, 2, 12, 120};
            maxminarray tc = new maxminarray();



    System.out.println(tc.getMaxArrayvlaue(array1));
    System.out.println(tc.getMinimumArray(array1));
   

    }
}
