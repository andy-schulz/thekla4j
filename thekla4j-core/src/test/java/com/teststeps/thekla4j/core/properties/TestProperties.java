package com.teststeps.thekla4j.core.properties;

import static com.teststeps.thekla4j.core.properties.TempFolderUtil.tempDir;

import io.vavr.Function0;
import io.vavr.control.Option;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class TestProperties {

  @Test
  public void testProperties() {
    Option<String> p = tempDir.get();
    System.out.println(p);

    System.out.println(Files.exists(Path.of(p.get())));
  }

  @Test
  public void testMomoization() {

    Function0<Double> randomNumber = Math::random;

    Function0<Double> memoizedRandomNumber = randomNumber.memoized();
    Function0<Double> memoizedRandomNumber2 = randomNumber.memoized();

    System.out.println("Memoized random number: " + memoizedRandomNumber.get());
    System.out.println("Memoized random number 2: " + memoizedRandomNumber2.get());

    System.out.println("Memoized random number: " + memoizedRandomNumber.get());
    System.out.println("Memoized random number 2: " + memoizedRandomNumber2.get());

  }
}
