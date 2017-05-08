package comperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;


public class ComperatorTest  {
	
	int hjkh=199;
	public static void main(String[] args)
	{
		ArrayList<Student> st = new ArrayList<Student>();
		st.add(new Student(123,"Akshay","EE"));
		st.add(new Student(136,"Ram","EE"));
		st.add(new Student(105,"Lambu","IT"));
		st.add(new Student(104,"Shishir","CS"));
		st.add(new Student(99,"Behl","EE"));
		st.add(new Student(101,"Deepak","EE"));
		st.add(new Student(35,"Dhar","ME"));
		
		Collections.sort(st, new comparebyName());
		
	for(Student ct:st)
	{
		System.out.println(ct.rollno+" "+ct.Name+" "+ct.Stream);
		
		
	}
		
		
	}

}




class Student
{
Integer rollno;
String Name;
String Stream;

public  Student(int rollno,String Name, String Stream)
{
this.rollno=rollno;
this.Name=Name;
this.Stream=Stream;
}

}

class comparebyName  implements Comparator<Student>
{
		@Override
		public int compare(Student s1, Student s2) {
				return (Integer)s1.rollno.compareTo((Integer)s2.rollno);
	}

	
	
}