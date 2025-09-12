package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.Function2;
import java.nio.file.Path;
import java.util.HashMap;
import org.openqa.selenium.chrome.ChromeOptions;


public class ChromeSpecificSetup {

  public static final Function2<Path, ChromeOptions, ChromeOptions> setFileDownloadDir = (downloadPath, options) -> {
    HashMap<String, Object> prefs = new HashMap<>();

    prefs.put("download.default_directory", TempFolderUtil.directory(downloadPath).toAbsolutePath().toString());
    prefs.put("download.prompt_for_download", false);

    options.setExperimentalOption("prefs", prefs);

    return options;
  };


}
