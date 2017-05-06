package DependencyInjection;

public class Application {
	public static void main(String[] args) 
	{
		Shape shape = new Triangle();
		shape.draw();
		Shape shape1 = new Circle();
		shape1.draw();
		
	}
}

