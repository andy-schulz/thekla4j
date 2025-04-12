package com.teststeps.thekla4j.utils.file;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Log4j2
public class FileUtils {

  public static Function1<String, Try<String>> readStringFromResourceFile =
    file ->
      Try.of(() -> FileUtils.class.getClassLoader().getResourceAsStream(file))
        .map(FileUtils::convertStreamToString);


  private static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

  public static Function2<Path, File, Try<File>> moveFile =
    (targetFolder, sourceFile) ->
      (Files.exists(targetFolder) ? Try.success(targetFolder) : Try.of(() -> Files.createDirectories(targetFolder)))

        .mapTry(folder -> Files.move(sourceFile.toPath(), folder.resolve(sourceFile.getName()), REPLACE_EXISTING))
        .map(Path::toString)
        .map(File::new)
        .onFailure(log::error);

  public static Function<File, String> readStringFromFile =
    file ->
      Try.of(() -> file)
        .map(File::toPath)
        .mapTry(Files::lines)
        .map(lines -> lines.collect(Collectors.joining("\n")))
        .getOrElseGet(e -> {log.error("Error reading file: {}", e.getMessage());return "";
        });

  public static Function1<String, Try<String>> readStringFromLocation =
    location ->
      Try.of(() -> location)
        .map(Path::of)
        .mapTry(Files::lines)
        .map(lines -> lines.collect(Collectors.joining("\n")));


}
