package ProgramsPorblems;

public class constructor2 {
    private int x;

    public constructor2() {
        this(1);
        System.out.println("no parameter");
    }

    public constructor2(int x) {
    	//this();  	 	
    	this.x = x;
 
    	
    	   	System.out.println("parameter");
    }
    
    public static void main(String[] args) {
    	
    	constructor2 g = new constructor2();
    
	}
}
