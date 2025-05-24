package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.appium.java_client.PullsFiles;
import io.appium.java_client.PushesFiles;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.Function6;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.RemoteWebDriver;

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

            Try<Void> downloadRemote = Try.run(() -> {
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

  private final static Function3<PushesFiles, Path, Path, Try<Void>> pushFile =
      (driver, devicePath, localFilePath) -> {

        log.info(() -> "Pushing file: " + localFilePath.getFileName() + " to device at path: " + devicePath);
        return Try.of(() -> devicePath.resolve(localFilePath.getFileName()).toString())
          .map(p -> p.replace("\\", "/"))
            .andThenTry(devPath -> driver.pushFile(devPath, localFilePath.toFile()))
            .onFailure(log::error)
            .map(__ -> null);
      };

  final static Function4<RemoteWebDriver, Option<Path>, List<Path>, Element, Try<Void>> uploadFiles =
      (driver, remotePath, files, inputElement) -> {

        Path devicePath = remotePath
            .getOrElse(Path.of(APPIUM_DOWNLOAD_FOLDER));

        log.info(() -> "Setting default upload path on device to: " + devicePath);

        String deviceFiles = files
          .map(Path::getFileName)
          .map(devicePath::resolve)
          .map(p -> p.toString().replace("\\", "/"))
          .mkString(",");

        return files.map(pushFile.apply((PushesFiles) driver, devicePath))
            .transform(LiftTry.fromList())
            .flatMap(__ -> ElementFunctions.findElementWithoutScrolling(driver, inputElement))
            .andThen(elem -> elem.sendKeys(deviceFiles))
            .onFailure(log::error)
            .map(__ -> null);

      };
}
