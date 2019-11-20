package com.seleniumsimplified.webdriver.remote;

import com.seleniumsimplified.environmentmanager.Props;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.Assert.*;

public class SaucelabsTest {

    public static WebDriver driver=null;

    @BeforeClass
    public static void setupSauce(){
        MutableCapabilities capabilities = new FirefoxOptions();
        capabilities.setCapability("platform", Platform.MAC);
        // add username and access key via environment variables
        // or property to avoid releasing with source
        capabilities.setCapability("username", Props.getEnvOrProperty("saucelabs.username"));
        capabilities.setCapability("accessKey", Props.getEnvOrProperty("saucelabs.accesskey"));

        try {
            String sauceURL = "https://ondemand.saucelabs.com/wd/hub";
            driver = new RemoteWebDriver(
                    new URL(sauceURL),
                    capabilities);
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

        assertTrue("Click selects",
                checkBox1.isSelected());
    }

    @Test
    public void findByIdOrNameMatchOnId(){

        driver.get("https://testpages.herokuapp.com/styled/find-by-playground-test.html");

        WebElement element;
        element = driver.findElement(
                new ByIdOrName("p3"));

        assertEquals("expected a match on id",
                "This is c paragraph text",
                element.getText());
    }

    @Test
    public void submitFormWithDropDown5SelectedChainOfFindElements(){

        WebElement dropDownSelect;
        WebElement dropDownOption;

        driver.get("https://testpages.herokuapp.com/styled/basic-html-form-test.html");

        dropDownSelect = driver.findElement(By.cssSelector("select[name='dropdown']"));
        dropDownOption = dropDownSelect.findElement(By.cssSelector("option[value='dd5']"));
        dropDownOption.click();

        clickSubmitButton();

        new WebDriverWait(driver,10).until(ExpectedConditions.titleIs("Processed Form Details"));

        assertDropdownValueIsCorrect();
    }

    @Test
    public void submitFormWithDropDown5SelectedOptionFiveDirect(){

        driver.get("https://testpages.herokuapp.com/styled/basic-html-form-test.html");

        driver.findElement(By.cssSelector("option[value='dd5']")).click();

        clickSubmitButton();

        new WebDriverWait(driver,10).until(ExpectedConditions.titleIs("Processed Form Details"));

        assertDropdownValueIsCorrect();
    }

    private void clickSubmitButton() {
        WebElement submitButton;
        submitButton = driver.findElement(
                By.cssSelector(
                        "input[type='submit'][name='submitbutton']"));

        submitButton.click();
    }

    private void assertDropdownValueIsCorrect() {
        WebElement dropDownResult;

        dropDownResult = driver.findElement(By.id("_valuedropdown"));

        assertEquals("expected drop down 5", dropDownResult.getText(), "dd5");
    }

    @AfterClass
    public static void stopSauce(){
        driver.quit();
    }
}
