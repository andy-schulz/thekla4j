package com.teststeps.thekla4j.activityLog;

import io.vavr.Function1;
import io.vavr.control.Try;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class TestFunctions {

  public static Function1<String, String> writeContentToIndexFile =
      content ->
          Try.of(() -> new PrintWriter("C:/temp" + File.separator + "index.html"))
              .peek(pw -> pw.print(""))
              .map(pw -> pw.append(content))
              .peek(PrintWriter::close)
              .map(pw -> "C:/temp" + File.separator + "index.html")
              .getOrElseThrow(x -> new RuntimeException("Could not load thekla4j.properties", x));



  public static Function1<String, String> readStringFromFile =
      file ->
          Try.of(() -> TestFunctions.class.getClassLoader().getResource(file))
              .mapTry(URL::toURI)
              .map(Path::of)
              .mapTry(Files::lines)
              .map(lines -> lines.collect(Collectors.joining("\n")))
              .getOrElseThrow(x -> new RuntimeException("could not load file " + file, x));

}
