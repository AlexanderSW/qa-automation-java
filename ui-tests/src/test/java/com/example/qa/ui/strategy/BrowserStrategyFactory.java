package com.example.qa.ui.strategy;

import com.example.qa.common.strategy.BrowserStrategy;

public class BrowserStrategyFactory {
    public static BrowserStrategy byName(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> new ChromeStrategy();
            default -> new ChromeStrategy();
        };
    }
}
