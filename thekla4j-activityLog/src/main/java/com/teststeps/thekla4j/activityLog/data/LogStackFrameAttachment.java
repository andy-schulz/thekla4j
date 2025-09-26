package com.teststeps.thekla4j.activityLog.data;

/**
 * A stack frame attachment to be added to the activity log
 */
public class LogStackFrameAttachment {
  private final String functionName;
  private final String url;
  private final int line;
  private final int column;

  /**
   * Creates a new stack frame attachment
   *
   * @param functionName the name of the function
   * @param url          the url of the file
   * @param line         the line number
   * @param column       the column number
   */
  public LogStackFrameAttachment(String functionName, String url, int line, int column) {
    this.functionName = functionName;
    this.url = url;
    this.line = line;
    this.column = column;
  }

  @Override
  public String toString() {
    return String.format("    at %s (%s:%d,%d)", functionName, url, line, column);
  }
}
