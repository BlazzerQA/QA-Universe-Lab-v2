package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static java.time.Duration.ofSeconds;

public class MainPage {

    public final SelenideElement header = $("[data-testid='app-header-title']");
    private final SelenideElement profileButton = $("[data-testid='header-menu']");
    private final SelenideElement logoutLink = $("[data-testid='logout-link']");

    public void logout() {
        profileButton.click();
        logoutLink.shouldBe(visible, ofSeconds(3));
        logoutLink.click();
    }
}
