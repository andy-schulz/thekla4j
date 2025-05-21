package com.teststeps.thekla4j.browser.config;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 * The options for the Chrome browser
 * 
 * @param binary   - the path to the binary
 * @param headless - if the browser should be
 * @param args     - the arguments for the browser
 * @param debug    - the address of the debugger
 */
public record ChromeOptions(
                            @JsonIgnore String binary,
                            Boolean headless,
                            List<String> args,
                            DebugOptions debug
) {

  /**
   * get a string representation of the chrome options
   */
  @Override
  public String toString() {
    return "ChromeOptions{" +
        "binary='" + binary + '\'' +
        ", headless=" + headless +
        ", args=" + args +
        ", debugOptions='" + debug + '\'' +
        '}';
  }

  public static String help() {
    return """
        binary: "/path/to/binary" # the path to the binary
        headless: true/false # if the browser should be headless
        args: [] # Example: ["--no-sandbox", "--disable-dev-shm-usage"]
        debug: # chrome debugging options
        {{DEBUG_OPTIONS}}
        """
        .replace("{{DEBUG_OPTIONS}}", DebugOptions.help().indent(2));
  }
}
