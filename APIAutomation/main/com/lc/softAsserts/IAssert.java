package com.lc.softAsserts;

public interface IAssert {
    public String getMessage();

    public void doAssert();

    public Object getActual();

    public Object getExpected();
}