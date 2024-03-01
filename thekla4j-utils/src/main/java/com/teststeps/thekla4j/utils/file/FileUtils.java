package com.teststeps.thekla4j.utils.file;

import io.vavr.Function1;
import io.vavr.control.Try;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Collectors;

public class FileUtils {

  public static Function1<String, Try<String>> readStringFromResourceFile =
    file ->
      Try.of(() -> FileUtils.class.getClassLoader().getResourceAsStream(file))
        .map(FileUtils::convertStreamToString);


  private static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }


  public static Function1<String, Try<String>> readStringFromLocation =
    location ->
      Try.of(() -> location)
        .map(Path::of)
        .mapTry(Files::lines)
        .map(lines -> lines.collect(Collectors.joining("\n")));


}
