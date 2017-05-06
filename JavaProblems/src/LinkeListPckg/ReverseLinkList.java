package LinkeListPckg;

public class ReverseLinkList {

	private Node head;

	private static class Node {
		private int value;
		private Node next;

		Node(int value) {
			this.value = value;

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
	
	Node reverse(Node node) {

		Node current = node;
		Node prev = null;
        Node next = null;
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        node = prev;
        return node;
    }

	public Node ReverseLinklist1(Node head) {
		
		Node currNode=head;
		Node prevNode=null;
		Node nextNode=null;
		
		
			
		while (currNode!=null)
		{
		 nextNode = currNode.next;
	     currNode.next = prevNode;
	     prevNode = currNode;
	     currNode = nextNode;		
		}		
		head = prevNode;
		System.out.println("\n Reverse Through Iteration");
        return head;
    	
	}

	public static void main(String[] args) {
		ReverseLinkList list = new ReverseLinkList();
		// Creating a linked list
		Node head = new Node(5);
		list.addToTheLast(head);
		list.addToTheLast(new Node(1));
		list.addToTheLast(new Node(2));
		list.addToTheLast(new Node(3));
		list.addToTheLast(new Node(4));
		list.addToTheLast(new Node(5));
		list.addToTheLast(new Node(6));
		list.addToTheLast(new Node(7));
		list.addToTheLast(new Node(8));
		list.addToTheLast(new Node(9));
		list.addToTheLast(new Node(10));
		list.addToTheLast(new Node(11));

		list.printList();
		// Finding 3rd node from end
		Node revlisthead = list.reverse(head);
		System.out.println("now head value is: " + revlisthead.value);
		Node temp = revlisthead;
		while (temp != null) {
			System.out.format("%d ", temp.value);
			temp = temp.next;
		}
		System.out.println();

		
		
	}

}

