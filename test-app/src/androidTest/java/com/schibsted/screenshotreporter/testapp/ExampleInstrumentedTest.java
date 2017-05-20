package com.schibsted.screenshotreporter.testapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.schibsted.screenshotreporter.testapp", appContext.getPackageName());
    }
}
