package com.lc.assertion;

import java.util.List;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Lists;

public class LoggingAssert extends Assertion {
	 
	  private List<String> m_messages = Lists.newArrayList();
	 
	  @Override
	  public void onBeforeAssert(IAssert a) {
	    m_messages.add("Test:" + a.getMessage());
	  }
	 
	  public List<String> getMessages() {
	    return m_messages;
	  }
	}

