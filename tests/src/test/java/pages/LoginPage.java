package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement phoneInput = $("[data-testid='login-phone']");
    private final SelenideElement passwordInput = $("[data-testid='login-password']");
    private final SelenideElement loginButton = $("[data-testid='login-submit']");
    private final SelenideElement errorMessage = $("[data-testid='login-error']");
    private final SelenideElement phoneError = $("[data-testid='login-phone-error']");
    private final SelenideElement loginTab = $("[data-testid='tab-login']");
    private final SelenideElement registerTab = $("[data-testid='tab-register']");
    private final SelenideElement regPhoneInput = $("[data-testid='register-phone']");
    private final SelenideElement regPasswordInput = $("[data-testid='register-password']");
    private final SelenideElement regConfirmPasswordInput = $("[data-testid='register-confirm']");
    private final SelenideElement registerButton = $("[data-testid='register-submit']");
    private final SelenideElement registerMessage = $("[data-testid='register-success']");
    private final SelenideElement registerError = $("[data-testid='register-error']");
    private final SelenideElement regPhoneError = $("[data-testid='register-phone-error']");
    private final SelenideElement regPasswordError = $("[data-testid='register-password-error']");
    private final SelenideElement regConfirmError = $("[data-testid='register-confirm-error']");

    public void login(String phone, String password) {
        phoneInput.setValue(phone);
        passwordInput.setValue(password);
        loginButton.shouldBe(visible).click();
    }

    public void checkErrorMessage(String expectedText) {
        errorMessage.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void checkPhoneError(String expectedText) {
        phoneError.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void shouldBeOnLoginPage() {
        loginTab.shouldBe(visible).shouldHave(text("Вход"));
    }

    public void switchToRegisterTab() {
        registerTab.shouldBe(visible).click();
    }

    public void register(String phone, String password, String confirmPassword) {
        regPhoneInput.setValue(phone);
        regPasswordInput.setValue(password);
        regConfirmPasswordInput.setValue(confirmPassword);
        registerButton.shouldBe(visible).click();
    }

    public void shouldSeeRegistrationSuccessMessage() {
        registerMessage.shouldBe(visible).shouldHave(text("Регистрация успешна! Теперь войдите."));
    }

    public void shouldSeeRegistrationErrorMessage(String expectedText) {
        registerError.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void shouldSeePhoneError(String expectedText) {
        regPhoneError.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void shouldSeePasswordError(String expectedText) {
        regPasswordError.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void shouldSeeConfirmError(String expectedText) {
        regConfirmError.shouldBe(visible).shouldHave(text(expectedText));
    }
}
