package com.schibsted.screenshotreporter.testapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;

import com.sloydev.screenshotreporter.espresso.Screenshot;
import com.sloydev.screenshotreporter.espresso.ScreenshotDirectory;
import com.sloydev.screenshotreporter.espresso.ScreenshotRule;
import com.sloydev.screenshotreporter.testapp.MainActivity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static com.schibsted.spain.barista.BaristaAssertions.assertDisplayed;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TakeScreenshotTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule();

    private File screenshotsDirectory = ScreenshotDirectory.INSTANCE.get();

    @Before
    public void setUp() throws Exception {
        //TODO delete whole directory
    }

    @Test
    public void take_screenshot() throws Exception {
        File expectedFile = new File(screenshotsDirectory, "TakeScreenshotTest.take_screenshot.png");
        expectedFile.delete();

        Screenshot.take();

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
    @Ignore
    public void take_screenshot_on_failure() throws Exception {
        assertDisplayed("Bye World");
    }

    @Test
    @Ignore
    public void not_take_screenshot_on_handled_failure() throws Exception {
        try {
            assertDisplayed("Bye World");
        } catch (NoMatchingViewException e) {
        }
    }

}
