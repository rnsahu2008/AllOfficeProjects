package ProgramsPorblems;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueTest {
	
public static void main(String[] args) {
	
	//Queue<Integer> q = new LinkedList<Integer>();
		
	PriorityQueue<String> q = new PriorityQueue<String>();
	
q.offer("N");
q.offer("A");
q.offer("C");
q.offer("B");
q.offer("K");
q.offer("D");
q.offer("N");
q.offer("Z");
q.offer("X");
q.offer("Y");

		
while(!q.isEmpty())
{

System.out.println(q);
System.out.println("Removed :--" + q.remove());
System.out.println(q);
System.out.println("");

}

}


}

