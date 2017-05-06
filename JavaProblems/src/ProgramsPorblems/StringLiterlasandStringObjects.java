package ProgramsPorblems;

public class StringLiterlasandStringObjects {
	public static void main(String[] args) {
		
		char ch[]={'a','b','c'};  

		String s1="abc";
		String s2="abc";
		String s3= new String("abc");
		String s4= new String("abc");
		String s5= new String(ch);		
		
		   String s=new String("Sachin");  
		  s.concat("Tendulkar");//concat() method appends the string at the end  
		   System.out.println(s);//will print Sachin because strings are immutable objects  
		   
		   String s10="hello";  
		   System.out.println(s10.substring(0,5));//he  
		   String s11="Java is a programming language. Java is a platform. Java is an Island.";    
		   s11.replace("Java","Kava");//replaces all occurrences of "Java" to "Kava"    
		   System.out.println(s11);    

		   String str2="Hello";
		   StringBuffer sb1=new StringBuffer("Hello");  
		   StringBuffer sb=new StringBuffer("Hello");
		   StringBuilder sb2=new StringBuilder("Hello");
		   
		   
		   
		   
		   if(str2==sb.toString())
			{System.out.println("str2 and sb same location");}
			else{System.out.println("str2 and sb not in same location");			}
		   
		   if(str2.equals(sb.toString()))
			{System.out.println("sb and str2 same data");}
			else{System.out.println("sb and str2 not same data");}
		   
		   

			   
			   
			   if(str2==sb2.toString())
				{System.out.println("str2 and sb2 same location");}
				else{System.out.println("str2 and sb2 not in same location");			}
			   
			   if(str2.equals(sb2.toString()))
				{System.out.println("sb2 and str2 same data");}
				else{System.out.println("sb2 and str2 not same data");}
			   
			
			
			
			if(sb1==sb)
			{System.out.println("sb and sb1 same location");			}
			else{System.out.println("sb and sb1 not same location");			}
		   
		   
		   
		   if(sb1.equals(sb))
			{System.out.println("sb and sb1 same data");}
			else{System.out.println("sb and sb1 not same data");			}   
		   sb.replace(1, 13333333, "Ram");
		   System.out.println(sb);//prints Hello Java 
		   
		   String str="Test me";
		   str="Test me again";
		   System.out.println(str);
		if(s3==s4)
		{System.out.println("same location");		}
		else{System.out.println("not same location");		}
		
		if(s1.equals(s5))
		{System.out.println("same data");}
		else{System.out.println("not same data");		}
		
		
		
	}
	

	}
