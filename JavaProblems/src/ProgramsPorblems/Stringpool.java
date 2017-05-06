package ProgramsPorblems;

public class Stringpool {
	public static void main(String[] args) {
		String s1="cat";
		String s2="cat";
//Both String hashcode will be same if String content is same
		System.out.println(s1.hashCode());
		System.out.println(s2.hashCode());
//		//	After changing content of s1 String hashcode will changed
		s1="dog";
		System.out.println(s1.hashCode());
		System.out.println(s2.hashCode());

}
}