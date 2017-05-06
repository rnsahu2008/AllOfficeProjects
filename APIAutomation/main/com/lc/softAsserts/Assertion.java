package com.lc.softAsserts;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.testng.Assert;
import org.testng.asserts.IAssert;
import org.testng.asserts.IAssertLifecycle;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Assertion
implements IAssertLifecycle {
    protected void doAssert(IAssert assertCommand) {
        this.onBeforeAssert(assertCommand);
        try {
            try {
                this.executeAssert(assertCommand);
                this.onAssertSuccess(assertCommand);
            }
            catch (AssertionError ex) {
                this.onAssertFailure(assertCommand, ex);
                throw ex;
            }
            Object var4_2 = null;
            this.onAfterAssert(assertCommand);
        }
        catch (Throwable var3_5) {
            Object var4_3 = null;
            this.onAfterAssert(assertCommand);
            throw var3_5;
        }
    }

    @Override
    public void executeAssert(IAssert assertCommand) {
        assertCommand.doAssert();
    }

    @Override
    public void onAssertSuccess(IAssert assertCommand) {
    }

    @Deprecated
    @Override
    public void onAssertFailure(IAssert assertCommand) {
    }

    @Override
    public void onAssertFailure(IAssert assertCommand, AssertionError ex) {
        this.onAssertFailure(assertCommand);
    }

    @Override
    public void onBeforeAssert(IAssert assertCommand) {
    }

    @Override
    public void onAfterAssert(IAssert assertCommand) {
    }

    public void assertTrue(final boolean condition, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertTrue(condition, message);
            }

            public Object getActual() {
                return condition;
            }

            public Object getExpected() {
                return true;
            }
        });
    }

    public void assertTrue(final boolean condition) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertTrue(condition);
            }

            public Object getActual() {
                return condition;
            }

            public Object getExpected() {
                return true;
            }
        });
    }

    public void assertFalse(final boolean condition, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertFalse(condition, message);
            }

            public Object getActual() {
                return condition;
            }

            public Object getExpected() {
                return false;
            }
        });
    }

    public void assertFalse(final boolean condition) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertFalse(condition);
            }

            public Object getActual() {
                return condition;
            }

            public Object getExpected() {
                return false;
            }
        });
    }

    public void fail(final String message, final Throwable realCause) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.fail(message, realCause);
            }
        });
    }

    public void fail(final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.fail(message);
            }
        });
    }

    public void fail() {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.fail();
            }
        });
    }

    public void assertEquals(final Object actual, final Object expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final Object actual, final Object expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final String actual, final String expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final String actual, final String expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final double actual, final double expected, final double delta, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, delta, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final double actual, final double expected, final double delta) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected, delta);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final float actual, final float expected, final float delta, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, delta, message);
            }

            public Object getActual() {
                return Float.valueOf(actual);
            }

            public Object getExpected() {
                return Float.valueOf(expected);
            }
        });
    }

    public void assertEquals(final float actual, final float expected, final float delta) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected, delta);
            }

            public Object getActual() {
                return Float.valueOf(actual);
            }

            public Object getExpected() {
                return Float.valueOf(expected);
            }
        });
    }

    public void assertEquals(final long actual, final long expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final long actual, final long expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final boolean actual, final boolean expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final boolean actual, final boolean expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final byte actual, final byte expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return Byte.valueOf(actual);
            }

            public Object getExpected() {
                return Byte.valueOf(expected);
            }
        });
    }

    public void assertEquals(final byte actual, final byte expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return Byte.valueOf(actual);
            }

            public Object getExpected() {
                return Byte.valueOf(expected);
            }
        });
    }

    public void assertEquals(final char actual, final char expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return Character.valueOf(actual);
            }

            public Object getExpected() {
                return Character.valueOf(expected);
            }
        });
    }

    public void assertEquals(final char actual, final char expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return Character.valueOf(actual);
            }

            public Object getExpected() {
                return Character.valueOf(expected);
            }
        });
    }

    public void assertEquals(final short actual, final short expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final short actual, final short expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final int actual, final int expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final int actual, final int expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertNotNull(final Object object) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotNull(object);
            }

            public Object getActual() {
                return object;
            }
        });
    }

    public void assertNotNull(final Object object, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotNull(object, message);
            }

            public Object getActual() {
                return object;
            }
        });
    }

    public void assertNull(final Object object) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNull(object);
            }

            public Object getActual() {
                return object;
            }
        });
    }

    public void assertNull(final Object object, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNull(object, message);
            }

            public Object getActual() {
                return object;
            }
        });
    }

    public void assertSame(final Object actual, final Object expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertSame(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertSame(final Object actual, final Object expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertSame(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertNotSame(final Object actual, final Object expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotSame(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertNotSame(final Object actual, final Object expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotSame(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final Collection<?> actual, final Collection<?> expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final Collection<?> actual, final Collection<?> expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final Object[] actual, final Object[] expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEqualsNoOrder(final Object[] actual, final Object[] expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEqualsNoOrder(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final Object[] actual, final Object[] expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEqualsNoOrder(final Object[] actual, final Object[] expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEqualsNoOrder(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final byte[] actual, final byte[] expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final byte[] actual, final byte[] expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final Set<?> actual, final Set<?> expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final Set<?> actual, final Set<?> expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertEquals(final Map<?, ?> actual, final Map<?, ?> expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertNotEquals(final Object actual, final Object expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotEquals(actual, expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertNotEquals(final Object actual, final Object expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotEquals(actual, expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    void assertNotEquals(final String actual, final String expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotEquals((Object)actual, (Object)expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    void assertNotEquals(final String actual, final String expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotEquals((Object)actual, (Object)expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    void assertNotEquals(final long actual, final long expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotEquals((Object)actual, (Object)expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    void assertNotEquals(final long actual, final long expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotEquals((Object)actual, (Object)expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    void assertNotEquals(final boolean actual, final boolean expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotEquals((Object)actual, (Object)expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    void assertNotEquals(final boolean actual, final boolean expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotEquals((Object)actual, (Object)expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    void assertNotEquals(final byte actual, final byte expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotEquals(Byte.valueOf(actual), Byte.valueOf(expected), message);
            }

            public Object getActual() {
                return Byte.valueOf(actual);
            }

            public Object getExpected() {
                return Byte.valueOf(expected);
            }
        });
    }

    void assertNotEquals(final byte actual, final byte expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotEquals(Byte.valueOf(actual), Byte.valueOf(expected));
            }

            public Object getActual() {
                return Byte.valueOf(actual);
            }

            public Object getExpected() {
                return Byte.valueOf(expected);
            }
        });
    }

    void assertNotEquals(final char actual, final char expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotEquals(Character.valueOf(actual), Character.valueOf(expected), message);
            }

            public Object getActual() {
                return Character.valueOf(actual);
            }

            public Object getExpected() {
                return Character.valueOf(expected);
            }
        });
    }

    void assertNotEquals(final char actual, final char expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotEquals(Character.valueOf(actual), Character.valueOf(expected));
            }

            public Object getActual() {
                return Character.valueOf(actual);
            }

            public Object getExpected() {
                return Character.valueOf(expected);
            }
        });
    }

    void assertNotEquals(final short actual, final short expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotEquals((Object)actual, (Object)expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    void assertNotEquals(final short actual, final short expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotEquals((Object)actual, (Object)expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    void assertNotEquals(final int actual, final int expected, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotEquals((Object)actual, (Object)expected, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    void assertNotEquals(final int actual, final int expected) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotEquals((Object)actual, (Object)expected);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertNotEquals(final float actual, final float expected, final float delta, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotEquals(actual, expected, delta, message);
            }

            public Object getActual() {
                return Float.valueOf(actual);
            }

            public Object getExpected() {
                return Float.valueOf(expected);
            }
        });
    }

    public void assertNotEquals(final float actual, final float expected, final float delta) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotEquals(actual, expected, delta);
            }

            public Object getActual() {
                return Float.valueOf(actual);
            }

            public Object getExpected() {
                return Float.valueOf(expected);
            }
        });
    }

    public void assertNotEquals(final double actual, final double expected, final double delta, final String message) {
        this.doAssert(new SimpleAssert(message){

            public void doAssert() {
                Assert.assertNotEquals(actual, expected, delta, message);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    public void assertNotEquals(final double actual, final double expected, final double delta) {
        this.doAssert(new SimpleAssert(null){

            public void doAssert() {
                Assert.assertNotEquals(actual, expected, delta);
            }

            public Object getActual() {
                return actual;
            }

            public Object getExpected() {
                return expected;
            }
        });
    }

    private static abstract class SimpleAssert
    implements IAssert {
        private final String m_message;

        public SimpleAssert(String message) {
            this.m_message = message;
        }

        public String getMessage() {
            return this.m_message;
        }

        public Object getActual() {
            return null;
        }

        public Object getExpected() {
            return null;
        }

        public abstract void doAssert();
    }

}