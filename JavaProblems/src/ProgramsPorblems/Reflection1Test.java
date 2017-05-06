package ProgramsPorblems;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Reflection1Test {

public static void main(String[] args) {
	
	Class reflectclass =  Reflection1.class;
	String classname =reflectclass.getName();
	
	System.out.println("Classname: "+classname + "\n");
	int classmodifier =reflectclass.getModifiers();
	System.out.println("Modifeir : " + Modifier.isPublic(classmodifier) + "\n");
	Class[] interfaces = reflectclass.getInterfaces();
	
	for (Class if1:interfaces )
	{
		System.out.println("Inteface name" + if1.getName());
		
	}
	
	Class classsuper = reflectclass.getSuperclass();	
	System.out.println("SuperClassName :  "+ classsuper.getName() + "\n");
	
 Method[] classmethod = reflectclass.getMethods();
 
 
 for (Method method :classmethod)
 {
	 System.out.println("MethodName:    " + method.getName() + "\n");
	 
	 if(method.getName().startsWith("get"))
	 {
		 System.out.println("getter Method");
		 
	 }

	 else if(method.getName().startsWith("set"))
	 {
		 System.out.println("setter Method");
		 
	 }
	 
	 System.out.println("return types: " + method.getReturnType());
	 
	 Class[] parametertypes = method.getParameterTypes();
	
	 for (Class parameter : parametertypes)
	 {
		System.out.println("Parameteres:   "+parameter.getName()); 
		 
	 }
	 
	 Constructor constructor = null;
	 Object constructor2 =null;
	 try {
		constructor=reflectclass.getConstructor(new Class[]{});
	} catch (NoSuchMethodException | SecurityException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	 try {
		constructor2=reflectclass.getConstructor(int.class,String.class).newInstance(12,"Random things");
	} catch (InstantiationException | IllegalAccessException
			| IllegalArgumentException | InvocationTargetException
			| NoSuchMethodException | SecurityException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		
	
	 
	 Class[] construtorparmeteres = constructor.getParameterTypes();
	 for(Class paramtere : construtorparmeteres)
		 
	 {
		 System.out.println("Constructor Parametres:  " +paramtere.getName());
		 
	 }
	
	 Reflection1 newReflection1 = null;
	// Recu Recu1 =null;
	 try {
		newReflection1 =(Reflection1) constructor.newInstance();
	} catch (InstantiationException | IllegalAccessException
			| IllegalArgumentException | InvocationTargetException e) 
	 {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 newReflection1.setname("XT");
	 System.out.println("Ref Class Name: " +newReflection1.getname());
	 Field privateStringName =null;
	 Reflection1 Ref1 = new Reflection1();
		 
		 String idcodeString ="idcode";
		
		 try {
			privateStringName = Reflection1.class.getDeclaredField(idcodeString);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 privateStringName.setAccessible(true);
		 String valueofName;
		try {
			valueofName = (String) privateStringName.get(Ref1);
			System.out.println("FieldName:   "+valueofName);
			String methodname="getprivate";
			Method privateMethod=Reflection1.class.getDeclaredMethod(methodname, null);
			privateMethod.setAccessible(true);
			String privateReturnVal =(String) privateMethod.invoke(Ref1, null);
			System.out.println("private Method name : "+ privateReturnVal);
			
	  Class[] methodParameters = new Class[]{Integer.TYPE, String.class};
	  Object[] params = new Object[]{new Integer(10), new String("Random")};
	  privateMethod = Reflection1.class.getDeclaredMethod("getOtherprivate", methodParameters);
	 privateMethod.setAccessible(true);
	 privateReturnVal = (String) privateMethod.invoke(Ref1, params);
	 System.out.println("Reflection1 Other Private Method: " + privateReturnVal);

			
			
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 
 }}}
		
		
	 





	
	