package com.teststeps.thekla4j.browser.selenium.logListener;

import com.teststeps.thekla4j.browser.core.logListener.StackFrame;

/**
 * Implementation of the StackFrame interface representing a stack frame in a call stack.
 */
public class SeleniumStackFrame implements StackFrame {
  private final String functionName;
  private final String url;
  private final int line;
  private final int column;


  /**
   * Factory method to create a StackFrame instance.
   *
   * @param functionName the name of the function
   * @param url          the URL of the source file
   * @param line         the line number in the source file
   * @param column       the column number in the source file
   * @return a new StackFrame instance
   */
  public static StackFrame of(String functionName, String url, int line, int column) {
    return new SeleniumStackFrame(functionName, url, line, column);
  }

  private SeleniumStackFrame(String functionName, String url, int line, int column) {
    this.functionName = functionName;
    this.url = url;
    this.line = line;
    this.column = column;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String functionName() {
    return functionName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String url() {
    return url;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int line() {
    return line;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int column() {
    return column;
  }
}
