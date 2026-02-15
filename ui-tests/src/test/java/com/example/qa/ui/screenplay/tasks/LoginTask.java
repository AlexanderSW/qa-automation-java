// ui-tests/src/test/java/com/example/qa/ui/screenplay/tasks/LoginTask.java
package com.example.qa.ui.screenplay.tasks;

import com.example.qa.common.builders.Credentials;
import com.example.qa.ui.pages.LoginPage;
import com.example.qa.ui.screenplay.Actor;
import com.example.qa.ui.screenplay.Task;
import io.qameta.allure.Step;

public class LoginTask implements Task {
    private final LoginPage page;
    private final Credentials creds;

    private LoginTask(LoginPage page, Credentials creds) {
        this.page = page;
        this.creds = creds;
    }

    public static LoginTask with(LoginPage page, Credentials creds) {
        return new LoginTask(page, creds);
    }

    @Override
    @Step("{actor.name} logs in")
    public void performAs(Actor actor) {
        page.login(creds);
    }
}
