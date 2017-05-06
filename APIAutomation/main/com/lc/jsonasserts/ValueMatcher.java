package com.lc.jsonasserts;

public interface ValueMatcher<T> {

    boolean equal(T o1, T o2);

}
