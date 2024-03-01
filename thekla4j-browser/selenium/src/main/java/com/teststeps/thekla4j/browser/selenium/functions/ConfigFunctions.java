package com.teststeps.thekla4j.browser.selenium.functions;

import com.teststeps.thekla4j.utils.file.FileUtils;
import io.vavr.Function0;
import io.vavr.control.Try;

public class ConfigFunctions {

  public static final Function0<Try<String>> loadSeleniumConfig =
    () -> FileUtils.readStringFromResourceFile.apply("seleniumConfig.yaml");
}
