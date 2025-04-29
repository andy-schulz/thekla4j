package com.teststeps.thekla4j.browser.config;

public record DebugOptions(

  /**
   * The address of the debugger
   * @param debuggerAddress - the address of the debugger
   * @retrurn the debugger address
   */
  String debuggerAddress,

  /**
   * The path to the download folder
   * @param downloadPath - the path to the download folder
   * @return the download path
   */
  String downloadPath
) {

  public static String help() {
    return """
      debuggerAddress: "localhost:9222"
      downloadPath: "absolute/path/to/downloads"
      """;
  }
}
