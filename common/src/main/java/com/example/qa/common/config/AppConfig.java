// common/src/main/java/com/example/qa/common/config/AppConfig.java
package com.example.qa.common.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "system:properties",
        "classpath:config.properties"
})
public interface AppConfig extends Config {
    @Key("base.url") @DefaultValue("http://178.150.3.212")
    String baseUrl();

    @Key("ui.browser") @DefaultValue("chrome")
    String uiBrowser();

    @Key("api.basePath") @DefaultValue("")
    String apiBasePath();
}
