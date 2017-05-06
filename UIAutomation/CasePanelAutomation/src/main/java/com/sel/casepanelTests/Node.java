package com.sel.casepanelTests;

public class Node 
{
	public String label;
	public String  state;
	public boolean visited=false;
	public Node(String l)
	{
		this.label=l;
	}
	public Node(String l,String state)
	{
		this.label=l;
		this.state=state;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return label.toString();
	}
}

