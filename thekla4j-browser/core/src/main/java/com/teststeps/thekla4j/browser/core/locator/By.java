package com.teststeps.thekla4j.browser.core.locator;

public class By {
    public static Locator css(String cssSelector) {
        return new CssLocator(cssSelector);
    }

    public static Locator id(String id) {
        return new IdLocator(id);
    }

    public static Locator xpath(String xpath) {
        return new XpathLocator(xpath);
    }

    public static Locator text(String text) {
        return new TextLocator(text);
    }
}
