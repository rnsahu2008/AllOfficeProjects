package LinkeListPckg;

public class CreatingNnode {

	Node1 node;
	
	public CreatingNnode() {
		node =new Node1();
	}
	
	public static void main(String[] args) {

		CreatingNnode list = new CreatingNnode();
		
		list.node.add(10);
		list.node.add(11);
		list.node.add(13);
		list.node.add(14);
		
		list.node.print();

	}
	
	

}


class Node1 {
	int data;
	Node1 next;
	Node1 head;

	Node1() {
		this.next=null;
	}
	
	Node1(int d) {
		this.data = d;
		//this.next = null;

	}
	
	public void add(int data){
		if(head==null){
			Node1 node1=new Node1(data);
			node1.next=null;
			head=node1;
		}else{
			Node1 temp=head;
			while(temp.next!=null){
				temp=temp.next;
			}
			Node1 node=new Node1(data);
			node.next=null;
			temp.next=node;
		}
	}
	
	void delete(int data){
		
	}
	
	void insert(int data){
		
	}
	
	void print(){
		Node1 temp=head;
		System.out.print("[");
		while(temp!=null){
			System.out.print(temp.data+",");
			temp=temp.next;
		}
		//System.out.print(temp.data+",");
		System.out.print("}");
	}
}
