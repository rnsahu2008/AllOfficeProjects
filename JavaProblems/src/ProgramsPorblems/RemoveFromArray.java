package ProgramsPorblems;

import java.util.ArrayList;
import java.util.List;

public class RemoveFromArray {
	
	public static void main(String[] args) {
		
		List<Integer> ls = new ArrayList();
		ls.add(4);
		ls.add(5);
		ls.add(6);
		
		System.out.println(ls);
		ls.remove(1);
		System.out.println(ls);
		ls.remove(new Integer(4));
		System.out.println(ls);
		
		
	}

}
