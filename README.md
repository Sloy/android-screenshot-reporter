# Android Screenshot Reporter

Take screenshot during your Espresso tests and see them later in a report.

The tool consists in two dependencies: a client library to take screenshots (based on [Falcon](https://github.com/jraska/Falcon)),
and a Gradle plugin to download those screenshots from a device and generate the report.

# Usage

Just use the `ScreenshotRule` in your Espresso tests:

```java
class MyTest {
  // ...
  @Rule
  public ScreenshotRule screenshotRule = new ScreenshotRule();

  @Test
  public void myTest(){
    // ...
    screenshotRule.takeScreenshot("tag");
    // ...
  }

}
```

# Install
Add the plugin and the client dependency to your project:

```gradle
buildscript {
    dependencies {
        classpath 'com.sloydev:screenshot-reporter-plugin:0.0.1'
    }
}

apply plugin: 'com.sloydev.screenshot-reporter'

dependencies {
  androidTestCompile 'com.sloydev:screenshot-reporter-espresso:0.0.1'
}
```

Now you can run your usual `connectedAndroidTest` tasks.

# How it works

Magic!

_No, really, I have to fill this section at some point._
