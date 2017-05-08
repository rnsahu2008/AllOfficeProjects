package LinkeListPckg;

import java.util.ArrayList;

public class AddRemoveInArraylist {

	public static void main(String[] args) {
		ArrayList<String> a = new ArrayList<>();
		a.add("zero");
		a.add("one");
		a.add("two");
		a.add("three");
		for (int i = 0; i < a.size(); i++) 
		{
			System.out.println(i + ": " + a.get(i));
		}
		
		a.add(3, "qqq");

		System.out.println("Add");
		for (int i = 0; i < a.size(); i++) {
			System.out.println(i + ": " + a.get(i));
		}
		System.out.println("Remove");
		//a.remove(0);

		for (int i = 0; i < a.size(); i++) {
			System.out.println(i + ": " + a.get(i));

		}

	}
}