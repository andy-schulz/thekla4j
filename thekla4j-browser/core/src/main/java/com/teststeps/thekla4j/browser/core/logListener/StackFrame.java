package com.teststeps.thekla4j.browser.core.logListener;

/**
 * Represents a stack frame in a log entry.
 */
public interface StackFrame {

  /**
   * Returns the name of the function in the stack frame.
   *
   * @return the function name
   */
  String functionName();

  /**
   * Returns the URL of the source file in the stack frame.
   *
   * @return the URL of the source file
   */
  String url();

  /**
   * Returns the line number in the source file.
   *
   * @return the line number
   */
  int line();

  /**
   * Returns the column number in the source file.
   *
   * @return the column number
   */
  int column();
}
