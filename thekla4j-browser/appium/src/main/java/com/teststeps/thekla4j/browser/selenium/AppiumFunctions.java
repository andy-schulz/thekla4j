package com.teststeps.thekla4j.browser.selenium;

import io.appium.java_client.PullsFiles;
import io.vavr.Function6;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

@Log4j2(topic = "Appium Functions")
public class AppiumFunctions {

  private static final String APPIUM_DOWNLOAD_FOLDER = "/sdcard/Download/";
  private static final String IOS_DOWNLOAD_FOLDER = "";


  final static Function6<PullsFiles, Path, String, Long, Instant, Duration, Try<File>> getDownloadedFiles =
    (driver, tempDir, fileName, fileSize, end, waitBetweenTrys) -> Try.run(() -> Thread.sleep(waitBetweenTrys.toMillis()))
      .flatMap(__ -> {
        if (Instant.now().isAfter(end)) {
          return Try.failure(new TimeoutException("File download timed out"));
        }

        Try<Void> downloadRemote = Try.run(() ->
        {
          Files.deleteIfExists(tempDir.resolve(fileName));
          byte[] pulledbase64 = driver.pullFile(APPIUM_DOWNLOAD_FOLDER + fileName);
          Files.write(tempDir.resolve(fileName), pulledbase64);
        });

        if (downloadRemote.isFailure()) {
          log.debug("Download failed with error: {}", downloadRemote.getCause().getMessage());
          log.debug("Failed to download file from remote site: {}, retrying ...", fileName);
          return AppiumFunctions.getDownloadedFiles.apply(driver, tempDir, fileName, fileSize, end, waitBetweenTrys);
        }

        File file = tempDir.resolve(fileName).toFile();

        if (!file.exists()) {
          log.debug("Downloaded File {} does not yet exist, retrying ...", fileName);
          return AppiumFunctions.getDownloadedFiles.apply(driver, tempDir, fileName, fileSize, end, waitBetweenTrys);
        }

        if (!fileSize.equals(file.length())) {
          return AppiumFunctions.getDownloadedFiles.apply(driver, tempDir, fileName, file.length(), end, waitBetweenTrys);
        }

        log.info("Downloaded file {} to '{}' with size {} bytes", file.getName(), file.getParent(), file.length());
        return Try.success(file);

      })
      .onFailure(log::error);
}
