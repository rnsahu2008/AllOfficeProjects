package com.lc.softAsserts;

import org.testng.asserts.IAssert;

public interface IAssertLifecycle {
    public void executeAssert(IAssert var1);

    public void onAssertSuccess(IAssert var1);

    public void onAssertFailure(IAssert var1);

    public void onAssertFailure(IAssert var1, AssertionError var2);

    public void onBeforeAssert(IAssert var1);

    public void onAfterAssert(IAssert var1);
}