package LinkeListPckg;

public class NthFromLastandAddNewNodeinbegin {

	private Node head;

	private static class Node {
		private int value;
		private Node next;

		public Node(int value) {

			this.value = value;
			this.next = null;
		}
	}

	public Node kthfromlast(Node head, int n) {
		Node firstNode = head;
		Node secNode = head;

		for (int i = 0; i < n; i++) {
			firstNode = firstNode.next;

		}
		while (firstNode != null) {
			firstNode = firstNode.next;
			secNode = secNode.next;

		}

		return secNode;
	}

	public void display(Node head) {
		Node n = head;
		while (n != null) {
			// System.out.print(" " + n.value);
			System.out.print(n.value + " ");

			n = n.next;
		}
	}

	// Add new node in beginning
	public Node AddnewNode(Node head, int value) {
		Node Newnode = new Node(value);
		Newnode.next = head;
		head = Newnode;
		return head;

	}

	// Add new node in beginning
	public Node AddnewNodeinLast(Node head, int value) {
		Node NewnodeinLast = new Node(value);
		Node current = head;
		while (current.next != null) {
			current = current.next;
		}

		current.next = NewnodeinLast;
		NewnodeinLast.next=null;
		return head;
	}

	// Add new node in After any element
	public void AddNewElement(Node previous, int value) {
		if (previous == null) {
			System.out.println("cant b null");
			return;
		}

		Node NewnodeinElemnet = new Node(value);
		NewnodeinElemnet.next = previous.next;
		previous.next = NewnodeinElemnet;
	}

	public static void main(String[] args)
	{
		NthFromLastandAddNewNodeinbegin ls = new NthFromLastandAddNewNodeinbegin();

		Node head = new Node(10);
		Node sec = new Node(11);
		Node third = new Node(12);
		Node fourth = new Node(14);
		Node fifth = new Node(15);
		Node sixth = new Node(16);
		Node seventh = new Node(17);
		head.next = sec;
		sec.next = third;
		third.next = fourth;
		fourth.next = fifth;
		fifth.next = sixth;
		sixth.next = seventh;
		seventh.next = null;
		ls.display(head);
		System.out.println();
		ls.AddnewNodeinLast(head, 15);
		ls.display(head);

	}
}
