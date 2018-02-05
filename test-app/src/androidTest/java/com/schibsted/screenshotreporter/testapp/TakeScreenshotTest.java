package com.schibsted.screenshotreporter.testapp;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.sloydev.screenshotreporter.espresso.FileUtilsKt;
import com.sloydev.screenshotreporter.espresso.Screenshot;
import com.sloydev.screenshotreporter.espresso.ScreenshotDirectory;
import com.sloydev.screenshotreporter.espresso.ScreenshotOnFailureRule;
import com.sloydev.screenshotreporter.testapp.MainActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

public class TakeScreenshotTest {

    private static final String CLASS_NAME = TakeScreenshotTest.class.getName();

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ScreenshotOnFailureRule screenshotRule = new ScreenshotOnFailureRule();

    private static File screenshotsDirectory = ScreenshotDirectory.INSTANCE.get();

    @BeforeClass
    public static void setUpClass() throws Exception {
        FileUtilsKt.deleteDirectory(screenshotsDirectory);
    }

    @Test
    public void take_screenshot() throws Exception {
        File expectedFile = new File(screenshotsDirectory,
                CLASS_NAME + "/take_screenshot/take_screenshot.png");

        Screenshot.take();

        assertTrue("The file wasn't created",
                expectedFile.exists());
    }

    @Test
    public void take_screenshot_with_name() throws Exception {
        File expectedFile = new File(screenshotsDirectory,
                CLASS_NAME + "/take_screenshot_with_name/custom name.png");

        Screenshot.take("custom name");

        assertTrue("The file wasn't created",
                expectedFile.exists());
    }

    @Test
    public void take_multiple_screenshot() throws Exception {
        File expectedFile1 = new File(screenshotsDirectory,
                CLASS_NAME + "/take_multiple_screenshot/first.png");
        File expectedFile2 = new File(screenshotsDirectory,
                CLASS_NAME + "/take_multiple_screenshot/second.png");

        Screenshot.take("first");
        Screenshot.take("second");

        assertTrue("The file wasn't created", expectedFile1.exists());
        assertTrue("The file wasn't created", expectedFile2.exists());
    }

    @Test
    public void take_screenshot_on_espresso_failure() throws Exception {
        File expectedFile = new File(screenshotsDirectory,
                CLASS_NAME + "/take_screenshot_on_espresso_failure/FAILURE.png");

        try {
            onView(withText("I don't exist")).check(matches(isDisplayed()));
        } catch (Exception ignored) {
        }

        assertTrue("The file wasn't created",
                expectedFile.exists());
    }

}
