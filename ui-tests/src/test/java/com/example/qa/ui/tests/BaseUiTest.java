// ui-tests/src/test/java/com/example/qa/ui/tests/BaseUiTest.java
package com.example.qa.ui.tests;

import com.codeborne.selenide.Selenide;
import com.example.qa.common.config.AppConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.open;
import com.example.qa.ui.strategy.BrowserStrategyFactory;

public abstract class BaseUiTest {
    protected final AppConfig cfg = ConfigFactory.create(AppConfig.class);

    @BeforeEach
    void setUp() {
        // мінімально: Selenide сам підніме драйвер
        BrowserStrategyFactory.byName(cfg.uiBrowser()).apply();
        open(cfg.baseUrl() + "/login.php");
        System.out.println(">>> baseUrl from Owner = " + cfg.baseUrl());
        System.out.println(">>> full url = " + (cfg.baseUrl() + "/login.php"));
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }
}
