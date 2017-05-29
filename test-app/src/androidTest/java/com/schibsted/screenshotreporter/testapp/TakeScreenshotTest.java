package com.schibsted.screenshotreporter.testapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;

import com.sloydev.screenshotreporter.espresso.ScreenshotRule;
import com.sloydev.screenshotreporter.testapp.MainActivity;

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

    @Test
    public void take_screenshot_on_failure() throws Exception {
        assertDisplayed("Bye World");
    }

    @Test
    public void not_take_screenshot_on_handled_failure() throws Exception {
        try {
            assertDisplayed("Bye World");
        } catch (NoMatchingViewException e) {
        }
    }

}
