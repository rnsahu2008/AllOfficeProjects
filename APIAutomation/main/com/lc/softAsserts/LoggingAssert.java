package com.lc.softAsserts;

import java.util.List;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Lists;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class LoggingAssert extends Assertion {
    private List<String> m_messages = Lists.newArrayList();

    @Override
    public void onBeforeAssert(IAssert a) {
        this.m_messages.add("Test:" + a.getMessage());
    }

    public List<String> getMessages() {
        return this.m_messages;
    }
}