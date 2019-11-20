package com.seleniumsimplified.webdriver.remote;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GridTest {

    public static WebDriver driver=null;

    @BeforeClass
    public static void connectToGrid(){

        //FirefoxOptions options = new FirefoxOptions();
        MutableCapabilities options = new ChromeOptions();

        // set additional capabilities for OS, version etc.
        //options.setCapability("platformName", "Mac");

        try {
            // add url to environment variables to avoid releasing with source
            driver = new RemoteWebDriver(
                    new URL("http://localhost:4444/wd/hub"),
                    options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simpleInteraction(){
       driver.get("https://testpages.herokuapp.com/styled/basic-html-form-test.html");

        WebElement checkBox1 = driver.findElement(
                By.cssSelector("input[value='cb1']"));

        assertFalse("Starts not selected",
                checkBox1.isSelected());

        checkBox1.click();

        assertTrue("Click selects", checkBox1.isSelected());
    }

    @AfterClass
    public static void stopGrid(){
        driver.quit();
    }
}
