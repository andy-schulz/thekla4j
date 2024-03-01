package com.teststeps.thekla4j.activityLog;

import io.vavr.Function1;
import io.vavr.control.Try;

import java.io.File;
import java.io.PrintWriter;

public class TestFunctions {

  public static Function1<String, String> writeContentToIndexFile =
      content ->
          Try.of(() -> new PrintWriter("C:/temp" + File.separator + "index.html"))
              .peek(pw -> pw.print(""))
              .map(pw -> pw.append(content))
              .peek(PrintWriter::close)
              .map(pw -> "C:/temp" + File.separator + "index.html")
              .getOrElseThrow(x -> new RuntimeException("Could not load thekla4j.properties", x));
}
