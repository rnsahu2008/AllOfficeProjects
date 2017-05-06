package ProgramsPorblems;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

public class QueeuImplementation {
	
	public static void main(String[] args) {
		

        Deque queue = new ArrayDeque<String>();
        queue.add("A");
        queue.add("B");
        queue.add("C");
        queue.add("D");
        queue.add("E");

        System.out.println("Initial Size of Deque :" + queue.size());

        // get value and does not remove element from Deque
        System.out.println("Deque peek :" + queue.peek());

        // get first value and remove that object from Deque
        System.out.println("Deque poll :" + queue.poll());

        System.out.println("Final Size of Deque :" + queue.size());
}
}
