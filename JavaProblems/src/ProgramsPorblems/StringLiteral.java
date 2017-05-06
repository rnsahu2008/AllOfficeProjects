package ProgramsPorblems;

public   class StringLiteral {
	
public static void main(String[] args) {
	String a="Test";
	String b="Test2";
	System.out.println(a+b);
	
	String c="Test";
	String d=c+"Test2";
	String e="TestTest2";
	System.out.println(d==e);
	
	String f="Test3";
	String g="Test4";
	String h="Test5";
	String result=f+g+h;
	System.out.println(result);
	
	String[] arr={"test1","test2","test3"};
	String i="test1";
	
	String s="abc";
	
	StringBuffer sb = new StringBuffer("Test");

	StringBuffer sb1 = new StringBuffer("Test");
	
	System.out.println(sb1.append(sb));
			
	
		if(arr[0]==i)
	{System.out.println("done");}
	else{System.out.println("not done");}
}

}
