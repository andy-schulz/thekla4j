package com.teststeps.thekla4j.http.integration.hr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.httpRequest.JavaNetHttpClient;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Get;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class IT_ResponseBytes {

  /** the first four bytes of a PNG file, 0x89 is not valid UTF-8 */
  private static final byte[] PNG_MAGIC = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47};

  /** the first two bytes of a gzip stream */
  private static final byte[] GZIP_MAGIC = new byte[]{(byte) 0x1F, (byte) 0x8B};

  private static String httpBinHost;

  @BeforeAll
  public static void setup() {
    httpBinHost = "http://localhost:3001";
  }

  private static Actor tester() {
    return Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));
  }

  private static byte[] firstBytes(byte[] bytes, int count) {
    byte[] first = new byte[count];
    System.arraycopy(bytes, 0, first, 0, count);
    return first;
  }

  @Test
  public void downloadBinaryContentToAFile(@TempDir Path downloadDir) throws ActivityError, IOException {

    Request image = Request.on(httpBinHost + "/image/png");
    Path downloaded = downloadDir.resolve("downloaded.png");

    HttpResult result = tester().attemptsTo(
      Get.from(image))
        .getOrElseThrow(Function.identity());

    assertThat("the bytes of the response are kept", result.responseBytes().isDefined(), is(true));

    byte[] bytes = result.responseBytes().get();

    assertThat("the response is a png", firstBytes(bytes, 4), equalTo(PNG_MAGIC));

    assertThat("all bytes are received", bytes.length,
      equalTo(Integer.parseInt(result.headers().get("content-length").get().head())));

    Files.write(downloaded, bytes);

    assertThat("the written file is byte identical to the response", Files.readAllBytes(downloaded), equalTo(bytes));
  }

  @Test
  public void readingBinaryContentAsStringLosesBytes() throws ActivityError {

    Request image = Request.on(httpBinHost + "/image/png");

    tester().attemptsTo(
      Get.from(image))

        .peek(r -> assertThat("the string body cannot be turned back into the response",
          r.response().getBytes(StandardCharsets.UTF_8), not(equalTo(r.responseBytes().get()))))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void downloadAnArbitraryNumberOfBytes() throws ActivityError {

    Request bytes = Request.on(httpBinHost + "/bytes/64");

    tester().attemptsTo(
      Get.from(bytes))

        .peek(r -> assertThat("all bytes are received", r.responseBytes().get().length, equalTo(64)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void decodeTextContentWithTheCharsetOfTheResponse() throws ActivityError {

    Request utf8 = Request.on(httpBinHost + "/encoding/utf8");

    tester().attemptsTo(
      Get.from(utf8))

        .peek(r -> assertThat("the charset of the Content-Type header is used", r.response(), containsString("∮")))

        .peek(r -> assertThat("the bytes are the utf-8 encoding of the string", r.responseBytes().get(),
          equalTo(r.response().getBytes(StandardCharsets.UTF_8))))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void aCompressedResponseIsNotDecompressed() throws ActivityError {

    Request gzipped = Request.on(httpBinHost + "/gzip");

    tester().attemptsTo(
      Get.from(gzipped))

        .peek(r -> assertThat("the response is compressed", r.headers().get("content-encoding").get().head(), equalTo("gzip")))

        .peek(r -> assertThat("the bytes are still the compressed ones", firstBytes(r.responseBytes().get(), 2), equalTo(GZIP_MAGIC)))

        .getOrElseThrow(Function.identity());
  }
}
