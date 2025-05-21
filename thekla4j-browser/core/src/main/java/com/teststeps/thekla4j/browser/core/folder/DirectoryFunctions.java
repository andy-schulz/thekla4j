package com.teststeps.thekla4j.browser.core.folder;

import static com.teststeps.thekla4j.browser.core.folder.DirectoryConstants.DOWNLOAD_PREFIX;

import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.control.Option;
import java.nio.file.Path;

public class DirectoryFunctions {

  public static Option<Path> createDownloadPath() {
    return Option.of(TempFolderUtil.newSubTempFolder(DOWNLOAD_PREFIX));
  }
}
