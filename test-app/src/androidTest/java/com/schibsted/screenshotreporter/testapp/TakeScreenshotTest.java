package com.schibsted.screenshotreporter.testapp;

import android.support.test.rule.ActivityTestRule;

import com.sloydev.espresso.ScreenshotRule;

import org.junit.Rule;
import org.junit.Test;

import static com.schibsted.spain.barista.BaristaAssertions.assertDisplayed;

public class TakeScreenshotTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule();

    @Test
    public void take_a_screenshot() throws Exception {
        assertDisplayed("Hello World!");

        screenshotRule.takeScreenshot("holi");
    }

}
