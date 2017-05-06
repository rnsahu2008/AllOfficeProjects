package LinkeListPckg;

public class RemoveDupliUnsortedList {

	Node head;

	static class Node {
		int data;
		Node next;

		Node(int d) {
			this.data = d;
			Node next = null;

		}
		

	}

	public void printlist() {
		Node temp = head;
		while (temp != null) {
			System.out.print(temp.data+", ");
			//System.out.format("%d %",temp);
			temp = temp.next;
		}
	}
	public void removedulicates() {
		Node p1 = head;
		Node p2 = head;
		Node p3;
		if (head == null) {
			return;
		}
		while (p1 != null && p1.next != null) 
		{
			//System.out.println(p1.data);10,11,8,10,12,12,8,16
			p2 = p1;
			System.out.println(p2.data);

			while (p2.next != null) {

				if (p1.data == p2.next.data) {

					p3 = p2.next;
					p2.next = p2.next.next;

				} else {
					p2 = p2.next;
				}

			}
			p1 = p1.next;
			//System.out.println(p1.data);
		}

	}

	public static void main(String[] args) {

		RemoveDupliUnsortedList list = new RemoveDupliUnsortedList();
		list.head = new Node(10);
		Node obj1 = new Node(11);
		Node obj2 = new Node(8);
		Node obj3 = new Node(10);
		Node obj4 = new Node(12);
	//	list.head=obj4;
		Node obj5 = new Node(12);
		Node obj6 = new Node(8);
		Node obj7 = new Node(16);
		list.head.next = obj1;
		obj1.next = obj2;
		obj2.next = obj3;
		obj3.next = obj4;
		obj4.next = obj5;
		obj5.next = obj6;
		obj6.next = obj7;
		obj7.next = null;
		
		list.removedulicates();
		list.printlist();

	}

}
