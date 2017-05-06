package ProgramsPorblems;

public class AssonlyNumerciValueFromString {

	public static void main(String[] args) {
	
	String str = "123e45,j, _122";
	int length =str.length();
	System.out.println(length);
	int result=0;
	int resultAscii=0;
	for (int i=0;i<length;i++)
	{
		Character cha = str.charAt(i);
		
		int asciiVal = (int) cha;
		
		if(asciiVal>=48 & asciiVal<58)
		{

			resultAscii=resultAscii+Character.getNumericValue(cha);
			
		}
		
		
		/*if(Character.isDigit(cha))
		{
			result=result+cha.getNumericValue(cha);
			
		}
		
		//System.out.println(result);
		
	}

	System.out.println(result);
*/	}
	
	System.out.println(resultAscii);
	//System.out.println(Character.getNumericValue(resultascii));

}
}