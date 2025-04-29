package com.teststeps.thekla4j.browser.config;

public record VideoConfig(
  /**
   * switch video recording on or off
   * @param record - true to record, false to not record
   * @return - video recording option
   */
  Boolean record,

  /**
   * the path to the video file
   * @param path - the path to the video file
   * @return - video file path
   */
  String relativePath,

  /**
   * the prefix of the video file
   * @param filePrefix - the prefix of the video file
   * @return - video file prefix
   */
  String filePrefix
) {

  public static String help() {

    return """
record: true / false
relativePath: "path/to/video"
filePrefix: "FilePrefix"
  """;

  }
}
