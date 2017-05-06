package comperator;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class CompratorSortArrylist {
public static void main(String[] args) {
			
	ArrayList<employee> al = new ArrayList<employee>();
	al.add(new employee("Ram3", 300, "Noida3"));
	al.add(new employee("Ram4", 400, "Noida4"));
	al.add(new employee("Yam1", 100, "Noida1"));
	al.add(new employee("Tam1", 100, "Noida1"));
	al.add(new employee("Ram2", 200, "Noida2"));
	Collections.sort(al,new mycomparatorString());
	for (employee e:al)
	{System.out.println(e.id+" "+ e.name+" "+e.address);
	
	}}}

class employee
{	String name;
	int id;
	String address;
employee(String name,int id,String address)
	{	this.name=name;
		this.id=id;
		this.address=address;	}}

class mycomparatorForid implements Comparator<employee> 

{
	@Override
	public int compare(employee o1, employee o2)
	{			 return ((Integer)o1.id).compareTo((Integer)o2.id); //For id value
		
}
}

class mycomparatorString implements Comparator<employee> 
	{
		@Override
		public int compare(employee o1, employee o2) {
		return o1.name.compareTo(o2.name);	//For string value
	
	}
	
}
