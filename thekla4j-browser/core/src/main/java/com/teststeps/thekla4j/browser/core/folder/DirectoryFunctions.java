package com.teststeps.thekla4j.browser.core.folder;

import com.teststeps.thekla4j.core.properties.TempFolderUtil;
import io.vavr.control.Option;

import java.nio.file.Path;

import static com.teststeps.thekla4j.browser.core.folder.DirectoryConstants.DOWNLOAD_PREFIX;

public class DirectoryFunctions {

  public static Option<Path> createDownloadPath() {
      return Option.of(TempFolderUtil.newSubTempFolder(DOWNLOAD_PREFIX));
  }
}
