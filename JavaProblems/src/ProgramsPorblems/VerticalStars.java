package ProgramsPorblems;

import java.util.Arrays;

public class VerticalStars {


	public static void print(){
		int k=9304;
		int m=k;
		
		int count =0;
		while (k>0){
			k=k/10;
			count++;
		}
		int[] c=new int[count];
		int p=0;
		
		while (m>0){
			c[p++]=m%10;
			m=m/10;
		}
		boolean exists=true;
		
		System.out.println(Arrays.toString(c));
		 
		 while(exists){
			 exists=false;
			 for(int i=c.length-1;i>=0;i--){
				if(c[i]>0){
					System.out.print("*");
					exists=true;
					c[i]=c[i]-1;
				}else{
					System.out.print(" ");
				}
				
			 }
			 System.out.println("");
		 }
		
		
	}

	public static void main(String[] args) {
		
		VerticalStars var = new VerticalStars();
		var.print();
		
	}
		
	}