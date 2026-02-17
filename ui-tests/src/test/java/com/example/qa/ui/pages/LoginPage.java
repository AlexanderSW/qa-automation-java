// ui-tests/src/test/java/com/example/qa/ui/pages/LoginPage.java
package com.example.qa.ui.pages;

import com.codeborne.selenide.SelenideElement;
import com.example.qa.common.builders.Credentials;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement username = $("input[id='login']");
    private final SelenideElement password = $("input[id='password']");
    private final SelenideElement submit   = $("button[onclick='doLogin()']");
    private final SelenideElement errorBox = $("div[id='form-error']");

    @Step("Login with username={creds.username}")
    public void login(Credentials creds) {
        username.shouldBe(visible).setValue(creds.username());
        password.shouldBe(visible).setValue(creds.password());
        submit.shouldBe(enabled).click();
    }

    @Step("Expect login error is visible")
    public void expectLoginError() {
        errorBox.shouldBe(visible);
    }

    @Step("Expect redirected from login page")
    public void expectRedirectedFromLogin() {
        com.codeborne.selenide.Selenide.webdriver()
                .shouldNotHave(com.codeborne.selenide.WebDriverConditions.urlContaining("login.php"));
    }
}
