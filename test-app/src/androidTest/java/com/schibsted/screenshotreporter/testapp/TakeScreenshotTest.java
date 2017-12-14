package com.schibsted.screenshotreporter.testapp;

import android.support.test.rule.ActivityTestRule;

import com.sloydev.screenshotreporter.espresso.Screenshot;
import com.sloydev.screenshotreporter.espresso.ScreenshotDirectory;
import com.sloydev.screenshotreporter.espresso.ScreenshotOnFailureRule;
import com.sloydev.screenshotreporter.testapp.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

public class TakeScreenshotTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ScreenshotOnFailureRule screenshotRule = new ScreenshotOnFailureRule();

    private File screenshotsDirectory = ScreenshotDirectory.INSTANCE.get();

    @Test
    public void take_screenshot() throws Exception {
        File expectedFile = new File(screenshotsDirectory, "TakeScreenshotTest.take_screenshot.png");
        expectedFile.delete();

        Screenshot.take();

        assertTrue("The file wasn't created",
                expectedFile.exists());
    }

    @Test
    public void take_screenshot_with_suffix() throws Exception {
        File expectedFile = new File(screenshotsDirectory, "TakeScreenshotTest.take_screenshot_with_suffix-SUFFIX.png");
        expectedFile.delete();

        Screenshot.take(null, "-SUFFIX");

        assertTrue("The file wasn't created",
                expectedFile.exists());
    }

    @Test
    public void take_screenshot_with_name() throws Exception {
        File expectedFile = new File(screenshotsDirectory, "custom name.png");
        expectedFile.delete();

        Screenshot.take("custom name");

        assertTrue("The file wasn't created",
                expectedFile.exists());
    }

    @Test
    public void take_screenshot_on_espresso_failure() throws Exception {
        File expectedFile = new File(screenshotsDirectory, "TakeScreenshotTest.take_screenshot_on_espresso_failure (FAILURE).png");
        expectedFile.delete();

        try {
            onView(withText("I don't exist")).check(matches(isDisplayed()));
        } catch (Exception ignored) {
        }

        assertTrue("The file wasn't created",
                expectedFile.exists());
    }

}
