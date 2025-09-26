package com.teststeps.thekla4j.browser.config;

/**
 * Configuration options for debugging the browser.
 *
 * @param debuggerAddress - the address of the debugger
 * @param downloadPath    - the path to the download folder
 */
public record DebugOptions(

                           /**
                            * The address of the debugger
                            * 
                            * @param debuggerAddress - the address of the debugger
                            * @retrurn the debugger address
                            */
                           String debuggerAddress,

                           /**
                            * The path to the download folder
                            * 
                            * @param downloadPath - the path to the download folder
                            * @return the download path
                            */
                           String downloadPath
) {

  /**
   * Returns a help string with example configuration options.
   *
   * @return a help string with example configuration options
   */
  public static String help() {
    return """
        debuggerAddress: "localhost:9222"
        downloadPath: "absolute/path/to/downloads"
        """;
  }
}
