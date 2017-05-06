package com.sel.casepanelTests;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;


public class Graph 
{
	public Node rootNode;
	public Node nodess;
	public ArrayList nodes=new ArrayList();
	public int[][] adjMatrix;//Edges will be represented as adjacency Matrix
	int size;
	public void setRootNode(Node n)
	{
		this.rootNode=n;
	}
	
	public Node getRootNode()
	{
		return this.rootNode;
	}
	
	public void addNode(Node n)
	{
		nodes.add(n);
	}

	
	public void connectNode(Node start,Node end)
	{
		if(adjMatrix==null)
		{
			size=nodes.size();
			adjMatrix=new int[size][size];
		}

		int startIndex=nodes.indexOf(start);
		int endIndex=nodes.indexOf(end);
		adjMatrix[startIndex][endIndex]=1;
		//adjMatrix[endIndex][startIndex]=1;
	}
	
	private Node getUnvisitedChildNode(Node n)
	{
		
		int index=nodes.indexOf(n);
		int j=0;
		while(j<size)
		{
			if(adjMatrix[index][j]==1 && ((Node)nodes.get(j)).visited==false)
			{
				return (Node)nodes.get(j);
			}
			j++;
		}
		return null;
	}
	
	
	public void bfs()
	{
		
		
		Queue q=new LinkedList();
		q.add(this.rootNode);
		printNode(this.rootNode);
		rootNode.visited=true;
		while(!q.isEmpty())
		{
			Node n=(Node)q.remove();
			Node child=null;
			while((child=getUnvisitedChildNode(n))!=null)
			{
				child.visited=true;
				printNode(child);
				q.add(child);
			}
		}
		//Clear visited property of nodes
		clearNodes();
	}
	
	
	public void dfs(Node target)
	{
		System.out.println("Root Node "+this.rootNode.state);
		System.out.println("Target Node "+target.state);
		
		Stack<Node> s=new Stack<Node>();
		s.push(this.rootNode);
		rootNode.visited=true;
		printNode(rootNode);
		while(!s.isEmpty())
		{
			Node n=(Node)s.peek();
			Node child=getUnvisitedChildNode(n);
			if(child!=null)
			{
				child.visited=true;
				printNode(child);
				s.push(child);
				if(child.toString().equalsIgnoreCase(target.toString())){
					System.out.println("Found a path:======");
					List<Node> templist = new ArrayList<Node>(s);
					for(Node ns : templist){
						System.out.print(ns.state);
					}
					System.out.println("Path Finished");
				}
			}
			else
			{
				Node n2 = s.pop();
				System.out.println("Removed Node "+n2.state);
			}
		}
	
		clearNodes();
	}
	
	private List<Node> printPaths(List<List<Node>> paths){
		System.out.println("Following are the paths ====");
		int pathsize =paths.get(0).size();
		
		List<Node> path1= paths.get(0);
		
		for(List<Node> path : paths){
			int pathsize2=path.size();
			if(pathsize>pathsize2)
			{
				path1=path;
				pathsize=pathsize2;
			}
			
			//printPath(path);
		}
		
		//printPath(path1);
		return path1;
	}

	private void printPath(List<Node> path) {
		for(Node node : path){
			System.out.print(node.state+ "-> ");
		}
		System.out.println();
	}

	public List<Node> dfs2(Node target){
		cleanupcycles();
		List<List<Node>> paths = findPaths(this.rootNode, target);
		return printPaths(paths);
	}

	private void cleanupcycles(){
		for(int i=0; i<adjMatrix.length;i++){
			adjMatrix[i][i] = 0;
		}
	}
	
	
	private List<Node> children(Node node){
		int index=nodes.indexOf(node);
		int j=0;
		List<Node> children = new ArrayList<Node>();
		while(j<size)
		{
			if(adjMatrix[index][j]==1 )
			{
				children.add((Node)nodes.get(j));
			}
			j++;
		}
		return children;
	
	}
	
	private List<List<Node>> findPaths(Node src, Node target){
		List<Node> children = children(src);
		List<List<Node>> paths = new ArrayList<List<Node>>();
		if(src.visited ){
			return paths;
		}
		src.visited = true;
		for(Node child : children){
			if(child.state.equals(target.state)){
				List<Node> temp = new ArrayList<Node>();
				temp.add(child);
				paths.add(temp);
				continue;
			}
			List<List<Node>> tempPaths = findPaths(child, target);
			for(List<Node> temp2 : tempPaths){
				temp2.add(0, child);
			}
			paths.addAll(tempPaths);
		}
		src.visited = false;
		return paths;
	}
	

	
	
	private void clearNodes()
	{
		int i=0;
		while(i<size)
		{
			Node n=(Node)nodes.get(i);
			n.visited=false;
			i++;
		}
	}

	private void printNode(Node n)
	{
		System.out.print(n.state+" ");
	}

	
	
	

}
 