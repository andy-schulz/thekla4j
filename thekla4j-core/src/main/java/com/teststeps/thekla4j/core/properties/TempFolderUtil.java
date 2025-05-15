package com.teststeps.thekla4j.core.properties;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.NonNull;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.Supplier;

import static com.teststeps.thekla4j.core.properties.DefaultThekla4jCoreProperties.TEMP_DIR_PATH;

public class TempFolderUtil {


  private static final Path userDir = Path.of(System.getProperty("user.dir"));
  private static final Path gradleDir = userDir.resolve("build");
  private static final Path mavenDir = userDir.resolve("target");

  public static Path newSubTempFolder(String prefix) {
    return Path.of(TEMP_DIR_PATH.value()).resolve(prefix + "_" + shortUUID.get());
  }

  /**
   * Get the current date and time as a string, usable in file names
   */
  private static String nowString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    LocalDateTime dateTime = LocalDateTime.now();
    return dateTime.format(formatter);
  }

  static Supplier<Path> baseTempPath = () ->
    Files.exists(gradleDir) ?
      gradleDir.resolve("thekla4j") :
      Files.exists(mavenDir) ?
        mavenDir.resolve("thekla4j") :
        Path.of(System.getProperty("java.io.tmpdir")).resolve("thekla4j");

  static Supplier<Option<String>> baseTempDir = () ->
    Option.of(baseTempPath.get())
      .map(Path::toString);


  /**
   * Get the path to save temporary files
   */
  static Supplier<Option<String>> tempDir = () ->

    Option.of(baseTempPath.get())
      .map(p -> p.resolve(nowString() + "_" + TempFolderUtil.shortUUID.get()).toString());

  /**
   * Create the temporary folder
   *
   * @return - the path to the temporary folder
   */
  public static Path directory(Path path) {
    return Try.of(() -> Files.createDirectories(path))
      .getOrElseThrow(e -> new RuntimeException("Could not create download directory " + path, e));
  }

  /**
   * Delete the temporary folder
   *
   * @return - a Try of Void
   */
  public static Try<Void> delete(Path path) {

    if (Files.notExists(path)) {
      return Try.success(null);
    }

    return Try.run(() -> Files.walkFileTree(path, new SimpleFileVisitor<>() {
      @Override
      public @NonNull FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public @NonNull FileVisitResult postVisitDirectory(@NonNull Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }
    }));
  }

  protected static final Supplier<String> shortUUID = () -> {

    String uuid = UUID.randomUUID().toString().replace("-", "");

    return List.range(0, 8)
      .map(__ -> Math.random() * 31)
      .map(Double::intValue)
      .map(i -> uuid.subSequence(i, i + 1))
      .map(CharSequence::toString)
      .foldLeft("", (s, i) -> s + i);
  };
}
