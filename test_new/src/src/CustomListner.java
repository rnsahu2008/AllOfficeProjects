package src;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(src.ListnerForMyAnnotation.class)
public class CustomListner {
	
	
  @Test
  @MyAnnotation(browser = "firefox")
  public void f() {
  }
}
