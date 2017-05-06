package ProgramsPorblems;
import java.util.*;
public class RemoveMiddleArrayELement{
public static void main (String[]args){

    int [] myArr = {5,8,9,10,11,5,7};
    int midE1 = (myArr.length-1)/2;// array index start with 0   
     int j = midE1;
    if(myArr.length %2 ==0){
      myArr[j] = myArr[midE1+2];
      j++;
      midE1 = midE1+2;
    }   

 for(int i=midE1;i<myArr.length-2;i++){
        myArr[j] = myArr[i+1];
        j++;
    }
System.out.print(Arrays.toString(myArr));

}}

