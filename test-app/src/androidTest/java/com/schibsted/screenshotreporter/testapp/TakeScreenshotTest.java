package com.schibsted.screenshotreporter.testapp;

import android.support.test.rule.ActivityTestRule;

import com.sloydev.espresso.ScreenshotRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static com.schibsted.spain.barista.BaristaAssertions.assertDisplayed;
import static org.junit.Assert.assertTrue;

public class TakeScreenshotTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule();

    @Test
    public void take_a_screenshot() throws Exception {
        assertDisplayed("Hello World!");

        File outputFile = screenshotRule.takeScreenshot("holi");

        assertTrue("The file wasn't created", outputFile.exists());
    }

}
