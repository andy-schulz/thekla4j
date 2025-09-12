package com.teststeps.thekla4j.activityLog.data;

public class LogStackFrameAttachment {
  private final String functionName;
  private final String url;
  private final int line;
  private final int column;

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
