package ProgramsPorblems;

import org.testng.annotations.Factory;


public class FactoryTestFactory 
{
	
	
	
    @Factory
    public Object[] factoryMethod() {
        return new Object[] { 
                                new FactoryTest("one"), 
                                new FactoryTest("two") 
                            };
    }
}

