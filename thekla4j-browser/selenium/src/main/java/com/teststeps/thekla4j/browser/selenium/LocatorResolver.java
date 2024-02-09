package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.locator.Locator;
import com.teststeps.thekla4j.browser.core.locator.LocatorType;
import org.openqa.selenium.By;

public enum LocatorResolver {
    CSS {
        @Override
        public By resolve(String locator) {
            return By.cssSelector(locator);
        }
    },
    ID {
        @Override
        public By resolve(String locator) {
            return By.cssSelector("#" + locator);
        }
    },
    XPATH {
        @Override
        public By resolve(String locator) {
            return By.xpath(locator);
        }
    },
    TEXT {
        @Override
        public By resolve(String locator) {
            return By.linkText(locator);
        }
    };

    public abstract By resolve(String locator);

    public static LocatorResolver from(LocatorType locatorType) {
        return LocatorResolver.valueOf(locatorType.name());
    }

    public static By resolve(Locator locator) {
        return LocatorResolver.valueOf(locator.type().name()).resolve(locator.locatorString());
    }
}
