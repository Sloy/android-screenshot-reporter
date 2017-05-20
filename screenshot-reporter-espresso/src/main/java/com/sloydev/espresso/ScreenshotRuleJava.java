package com.sloydev.espresso;


import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ScreenshotRuleJava implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        return base;
    }

    public void takeScreenshot(String holi) {

    }
}
