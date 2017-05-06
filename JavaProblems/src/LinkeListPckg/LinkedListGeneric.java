package LinkeListPckg;

public class LinkedListGeneric<T> {

	Node<T>[] linkedlist;
	private int currentIndex = 0;

	public LinkedListGeneric() {
		linkedlist = new Node[10];
	}

	public boolean add(T o) {

		Node<T> node = new Node<T>();
		node.item = o;
		node.next = null;

		if (isEmpty()) {
			node.prev = null;
		} else {
			Node<T> preNode = linkedlist[currentIndex];
			node.prev = preNode;
		}
		linkedlist[currentIndex++] = node;

		return true;
	}

	public boolean delete(T node) {

		return false;
	}

	public boolean contains(T node) {
		boolean found=false; 
		
		for (int i = 0; i < linkedlist.length; i++) {
			if (linkedlist[i] != null) {
				Node<T> node1=(Node)linkedlist[i];
				if(node1.item.equals(node)){
					found=true;
					break;
				}
				
			}
		}
		
		return found;
	}
	

	public boolean isEmpty() {

		return this.linkedlist[0] == null;
	}

	public void printAll() {

		System.out.print("[");
		for (int i = 0; i < linkedlist.length; i++) {
			if (linkedlist[i] == null) {
				System.out.print("null, ");
			} else
				System.out.print(((Node<T>) linkedlist[i]).item + ", ");
		}
		System.out.print("]");
		System.out.println();

	}

	class Node<T> {

		T item;
		Node<T> next;
		Node<T> prev;

	}

}
