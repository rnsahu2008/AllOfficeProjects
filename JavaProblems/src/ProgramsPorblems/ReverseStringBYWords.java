package ProgramsPorblems;
public class ReverseStringBYWords {

		public void reverWord(String str1)
		
		{
		       String str[] = str1.split(" ");
		        String finalStr="";
		        int l =str.length;
		        System.out.println(l);
		            for(int i = str.length-1; i>= 0 ;i--){
		                finalStr += str[i]+" ";
		            }
		            System.out.println(finalStr);
		        }

		public static void main(String[] args) 
		{
			ReverseStringBYWords rs = new ReverseStringBYWords();
			//rs.ReveseStringMe1("Test ram");
			rs.reverWord("Test    is Test1");
			
		}

	}
