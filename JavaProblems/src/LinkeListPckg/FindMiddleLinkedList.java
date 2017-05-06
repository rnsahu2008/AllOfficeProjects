package LinkeListPckg;

import comperator.ComperabaleTest;


public class FindMiddleLinkedList  {  
	  
	int size=0;
	 private Node head;  
	  
	  static class Node {  
	   int value;  
	   Node next;  
	  
	  Node(int value) {  
	   this.value = value;  
	   this.next=null;
	  
	  }  
	 }  
	  
	 public void addToTheLast(Node node) {  
	  
		 
	  if (head == null) {  
	   head = node;  
	  } else {  
	   Node temp = head;  
	   while (temp.next != null)  
	    temp = temp.next;  
	  
	   temp.next = node;  
	   size++;
	  }  
	 }  
	  
	  
	 public void printList() {  
	  Node temp = head;  
	  while (temp != null) {  
	   System.out.format("%d ", temp.value);  
	   temp = temp.next;  
	  }  
	  System.out.println();  
	 }  
	  
	// This function will find middle element in linkedlist  
	 public Node findMiddleNode(Node head)  
	 {  
	 Node slowPointer=head;
	 Node fastPointer=head;
	//  slowPointer = fastPointer = head;   
	  
	  while(fastPointer !=null) {   
		  System.out.println(slowPointer.value);
		  System.out.println(fastPointer.value);
	  fastPointer = fastPointer.next;   
	   if(fastPointer != null && fastPointer.next != null) {   
	    slowPointer = slowPointer.next;   
	    fastPointer = fastPointer.next;   
	   }   
	  }   
	  
	  return slowPointer;   
	  
	 }  
	  
	 public static void main(String[] args) {  
		 FindMiddleLinkedList list = new FindMiddleLinkedList(); 
		 
		 
	  // Creating a linked list  
	  Node head=new Node(5);  
	  list.addToTheLast(head);  
	  list.addToTheLast(new Node(6));  
	  list.addToTheLast(new Node(7));  
	  list.addToTheLast(new Node(1));  
	  list.addToTheLast(new Node(2)); 

	  list.addToTheLast(new Node(11));  
	  list.addToTheLast(new Node(12)); 
	   
	  list.printList();  
	  // finding middle element  
	  Node middle= list.findMiddleNode(head);  
	  System.out.println("Middle node will be: "+ middle.value);  
	  
	 }  
	  
	}  
