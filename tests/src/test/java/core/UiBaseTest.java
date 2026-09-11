package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class UiBaseTest {

    @BeforeMethod
    public void uiSetup() {
        Configuration.baseUrl = System.getProperty("ui.baseUrl", "http://localhost:5173");
        Configuration.browser = System.getProperty("selenide.browser", "chrome");
        Configuration.headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "false"));
        Configuration.timeout = 5000;
        Configuration.reportsFolder = "target/allure-results/screenshots";

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));
    }

    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
