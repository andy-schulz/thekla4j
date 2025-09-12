package com.teststeps.thekla4j.browser.selenium.logListener;

import com.teststeps.thekla4j.browser.core.logListener.StackFrame;

public class SeleniumStackFrame implements StackFrame {
  public String functionName;
  public String url;
  public int line;
  public int column;


  public static StackFrame of(String functionName, String url, int line, int column) {
    return new SeleniumStackFrame(functionName, url, line, column);
  }

  private SeleniumStackFrame(String functionName, String url, int line, int column) {
    this.functionName = functionName;
    this.url = url;
    this.line = line;
    this.column = column;
  }

  @Override
  public String functionName() {
    return functionName;
  }

  @Override
  public String url() {
    return url;
  }

  @Override
  public int line() {
    return line;
  }

  @Override
  public int column() {
    return column;
  }
}
