package comperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class ComperabaleTest  {
	 
	 
	 public final int a=7;
	public static void main(String[] args) {
		
	
	ArrayList<Student1> st1 = new ArrayList<Student1>();
	st1.add(new Student1(123,"Akshay","EE"));
	st1.add(new Student1(136,"Ram","EE"));
	st1.add(new Student1(105,"Lambu","IT"));
	st1.add(new Student1(104,"Shishir","CS"));
	st1.add(new Student1(99,"Behl","EE"));
	st1.add(new Student1(101,"Deepak","EE"));
	st1.add(new Student1(35,"Dhar","ME"));
	Collections.sort(st1);
	
for (Student1 ud:st1)
{
	System.out.println(ud.rollno+" "+ud.Name+ " " + ud.Stream);
}
	
}


}

class Student1  implements Comparable<Student1>
{
Integer rollno;
String Name;
String Stream;

public int getRollno() {
	return rollno;
}

public void setRollno(int rollno) {
	this.rollno = rollno;
}

public String getName() {
	return Name;
}

public void setName(String name) {
	Name = name;
}

public String getStream() {
	return Stream;
}

public void setStream(String stream) {
	Stream = stream;
}

public  Student1(int rollno,String Name, String Stream)
{
this.rollno=rollno;
this.Name=Name;
this.Stream=Stream;
}

@Override
public int compareTo(Student1 stu) {
	
	return Stream.compareTo(stu.Stream);
}



/*@Override
public i	nt compareTo(Student1 stu) {
	
	int comparerollno = ((Student1) stu).getRollno();
	return this.rollno-comparerollno;

	//ascending order
//	return comparerollno-this.rollno;

}
	*/
}
