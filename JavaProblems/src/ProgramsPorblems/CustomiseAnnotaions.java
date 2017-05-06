package ProgramsPorblems;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Smarphone

{
	String os() default "unix";

	int version() default 4;

}

@Smarphone(os="Android",version=6)
class NokiaSeries 
{
	String model;
	int size;

	public NokiaSeries(String model, int size)
		{		this.model = model;
				this.size = size;
		}}

@Smarphone(os = "windows", version = 2)
public class CustomiseAnnotaions {
	public static void main(String[] args) {

		NokiaSeries obj = new NokiaSeries("Fire",5);
		Class c =obj.getClass();
		Annotation an=c.getAnnotation(Smarphone.class);
		Smarphone s = (Smarphone)an;
		System.out.println(s.os());
		
	}

}