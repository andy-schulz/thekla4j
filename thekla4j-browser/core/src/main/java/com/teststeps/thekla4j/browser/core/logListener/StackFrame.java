package com.teststeps.thekla4j.browser.core.logListener;

public interface StackFrame {
  String functionName();

  String url();

  int line();

  int column();
}
