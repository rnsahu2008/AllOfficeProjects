package DependencyInjection;

public class Application1 {
	
	public static void MydrawMethod(Shape shape)

	{
		
		shape.draw();
	}
public static void main(String[] args)
{
	Shape shape2 = new Triangle();
	MydrawMethod(shape2);

	
}

}
