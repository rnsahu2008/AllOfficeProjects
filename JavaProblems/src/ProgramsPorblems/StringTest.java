package ProgramsPorblems;

public class StringTest {
	
	public static void main(String[] args) {
		
		/*
		 String s1="BCD";

		String s2="B"+"C"+"D";
		String s3 = new String("BCD");
		
		
		System.out.println(s1==s2);
		System.out.println(s1.hashCode());
		System.out.println(s2.hashCode());
		System.out.println(s3.hashCode());
		
		*/
		
		StringBuffer sb = new StringBuffer("BCD");
		StringBuffer sb2=sb.append("T");
		
		System.out.println(sb2.equals(sb));
		System.out.println(sb.hashCode());
		System.out.println(sb2.hashCode());
		
		
	/*
		String s1="abc";
		String s2="def";
		String s3=s1+s2; // 
		String s4 = new String("abcdef");
		System.out.println(s3==s4);
		System.out.println(s3.equals(s4));
*/		
		/*StringBuffer sb=new StringBuffer("abc");
		String s ="abc";
		
		System.out.println(sb.toString().equals(s));
		System.out.println(sb.hashCode());
		System.out.println(s.hashCode());

*/
	
	
	}

}
