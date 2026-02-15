package com.example.qa.ui.strategy;

import com.codeborne.selenide.Configuration;
import com.example.qa.common.strategy.BrowserStrategy;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeStrategy implements BrowserStrategy {
    @Override
    public void apply() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-features=HttpsUpgrades,HTTPS-FirstMode");
        Configuration.browser = "chrome";
        Configuration.browserCapabilities = options;
        Configuration.timeout = 10_000;
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
    }
}
