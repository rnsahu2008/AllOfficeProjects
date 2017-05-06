package comperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompratorSortArrylist1 {
public static void main(String[] args) {
			
	List<Customer> al = new ArrayList<Customer>();

	al.add(new Customer("Ram3", 300, "Noida3"));
	al.add(new Customer("Ram4", 400, "Noida4"));
	al.add(new Customer("Yam1", 100, "Noida1"));
	al.add(new Customer("Tam1", 100, "Noida1"));
	al.add(new Customer("Ram2", 200, "Noida2"));
//	Collections.sort(al,new empl());
	
	Collections.sort(al);

	for (Customer e:al)
	{System.out.println(e.id+" "+ e.name+" "+e.address);
	
	}
	
}
}
class Customer implements Comparable<Customer>

{
	
	String name;
	Integer id;
	String address;
	
	
public Customer(String name,int id,String address)
	{
		this.name=name;
		this.id=id;
		this.address=address;
		
	}


@Override
public int compareTo(Customer emp) {
	
	return this.id.compareTo(emp.id) ;
}
	
}
