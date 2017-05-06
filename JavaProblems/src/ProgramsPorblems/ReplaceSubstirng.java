package ProgramsPorblems;

public class ReplaceSubstirng {
	
	
	public static String replace(String old, String newWord, String input) {    
	    int i = input.indexOf(old);
	    if (i < 0) {
	        return input;
	    }
	    String partBefore = input.substring(0, i);
	    String partAfter  = input.substring(i + old.length());
	    return partBefore + newWord + replace(old, newWord, partAfter );
	}

	public static void main(String[] args) {
		
		
		String phrase="iamthe";
		int i=phrase.length();
		String mi= phrase.substring (phrase.length()/2 - 1, phrase.length()/2 + 2);
System.out.println(mi);
				
		
		ReplaceSubstirng rsb = new ReplaceSubstirng();
		System.out.println(rsb.replace("ab", "zzz", "abcdabefab"));
		
	}
}
