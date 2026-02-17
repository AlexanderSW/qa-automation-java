// ui-tests/src/test/java/com/example/qa/ui/tests/LoginUiTest.java
package com.example.qa.ui.tests;

import com.example.qa.common.builders.Credentials;
import com.example.qa.ui.factory.PageFactory;
import com.example.qa.ui.screenplay.Actor;
import com.example.qa.ui.screenplay.tasks.ExpectLoginSuccessTask;
import com.example.qa.ui.screenplay.tasks.LoginTask;
import org.junit.jupiter.api.Test;

public class LoginUiTest extends BaseUiTest {
    private final PageFactory pages = new PageFactory();

    @Test
    void shouldShowErrorOnInvalidLogin() {
        var login = pages.loginPage();
        var actor = new Actor("QA");
        var creds = Credentials.builder()
                .username("admin")
                .password("210785ap")
                .build();

        actor.attemptsTo(LoginTask.with(login, creds), ExpectLoginSuccessTask.on(login));
        login.expectLoginError();
    }

    @Test
    void shouldShowErrorOnEmptyPassword() {
        var login = pages.loginPage();
        var actor = new Actor("QA");
        var creds = Credentials.builder()
                .username("admin")
                .password("")
                .build();

        actor.attemptsTo(LoginTask.with(login, creds), ExpectLoginSuccessTask.on(login));
        login.expectLoginError();
    }

    @Test
    void shouldShowErrorOnEmptyUsername() {
        var login = pages.loginPage();
        var actor = new Actor("QA");
        var creds = Credentials.builder()
                .username("")
                .password("210785ap")
                .build();

        actor.attemptsTo(LoginTask.with(login, creds), ExpectLoginSuccessTask.on(login));
        login.expectLoginError();
    }

    @Test
    void shouldLoginOnValidCredentials_example() {
        // Приклад: підставите валідні креденшали через -Duser= -Dpass= або секрети CI
        var user = System.getProperty("user", "admin");
        var pass = System.getProperty("pass", "210785");

        var login = pages.loginPage();
        login.login(Credentials.builder().username(user).password(pass).build());

        // Далі — assert по URL/елементу дашборда (під ваш реальний UI):
        // $("h1").shouldHave(text("Dashboard"));
    }

    @Test
    void shouldLoginOnValidCredentials_screenplay() {
        var user = System.getProperty("user", "admin");
        var pass = System.getProperty("pass", "210785");

        var login = pages.loginPage();
        var actor = new Actor("QA");
        var creds = Credentials.builder()
                .username(user)
                .password(pass)
                .build();

        actor.attemptsTo(LoginTask.with(login, creds), ExpectLoginSuccessTask.on(login));
    }
}
