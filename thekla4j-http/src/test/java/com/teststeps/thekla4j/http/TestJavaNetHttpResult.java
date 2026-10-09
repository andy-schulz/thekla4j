package com.teststeps.thekla4j.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.http.httpConn.HcHttpResult;
import com.teststeps.thekla4j.http.httpRequest.JavaNetHttpResult;
import io.vavr.control.Option;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("A JavaNetHttpResult")
public class TestJavaNetHttpResult {

  /** the first four bytes of a PNG file, 0x89 is not valid UTF-8 */
  private static final byte[] PNG_MAGIC = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47};

  @SuppressWarnings("unchecked")
  private static JavaNetHttpResult resultOf(byte[] body, String contentType) {
    HttpResponse<byte[]> response = mock(HttpResponse.class);

    when(response.body()).thenReturn(body);
    when(response.statusCode()).thenReturn(200);
    when(response.headers()).thenReturn(
      HttpHeaders.of(
        contentType == null ? Map.of() : Map.of("Content-Type", List.of(contentType)),
        (name, value) -> true));

    return new JavaNetHttpResult(response);
  }

  @Test
  @DisplayName("keeps binary content byte for byte")
  public void responseBytesKeepsBinaryContent() {
    JavaNetHttpResult result = resultOf(PNG_MAGIC, "image/png");

    assertThat("bytes are kept", result.responseBytes().get(), equalTo(PNG_MAGIC));
  }

  @Test
  @DisplayName("loses binary content when it is read as a string")
  public void responseLosesBinaryContent() {
    JavaNetHttpResult result = resultOf(PNG_MAGIC, "image/png");

    assertThat("the invalid byte is replaced", result.response(), containsString("�"));
    assertThat("re-encoding the string does not give the bytes back",
      result.response().getBytes(StandardCharsets.UTF_8), not(equalTo(PNG_MAGIC)));
  }

  @Test
  @DisplayName("has an empty byte array for an empty body")
  public void responseBytesOfEmptyBody() {
    JavaNetHttpResult result = resultOf(new byte[0], "application/json");

    assertThat("bytes are present", result.responseBytes().isDefined(), is(true));
    assertThat("bytes are empty", result.responseBytes().get().length, equalTo(0));
    assertThat("string is empty", result.response(), equalTo(""));
  }

  @Test
  @DisplayName("decodes the body as UTF-8 when there is no Content-Type header")
  public void decodesAsUtf8WithoutContentType() {
    JavaNetHttpResult result = resultOf("Grüße".getBytes(StandardCharsets.UTF_8), null);

    assertThat("body is decoded", result.response(), equalTo("Grüße"));
  }

  @Test
  @DisplayName("decodes the body as UTF-8 when the Content-Type has no charset parameter")
  public void decodesAsUtf8WithoutCharsetParameter() {
    JavaNetHttpResult result = resultOf("Grüße".getBytes(StandardCharsets.UTF_8), "application/json");

    assertThat("body is decoded", result.response(), equalTo("Grüße"));
  }

  @Test
  @DisplayName("decodes the body with the charset of the Content-Type header")
  public void decodesWithCharsetOfContentType() {
    JavaNetHttpResult result = resultOf("Grüße".getBytes(StandardCharsets.ISO_8859_1), "text/plain; charset=ISO-8859-1");

    assertThat("body is decoded", result.response(), equalTo("Grüße"));
  }

  @Test
  @DisplayName("finds the charset parameter behind other parameters and ignores its case")
  public void findsCharsetParameterBehindOtherParameters() {
    JavaNetHttpResult result = resultOf("Grüße".getBytes(StandardCharsets.ISO_8859_1), "text/plain; boundary=xyz; CHARSET=iso-8859-1");

    assertThat("body is decoded", result.response(), equalTo("Grüße"));
  }

  @Test
  @DisplayName("accepts a quoted charset parameter")
  public void acceptsQuotedCharsetParameter() {
    JavaNetHttpResult result = resultOf("Grüße".getBytes(StandardCharsets.ISO_8859_1), "text/plain; charset=\"ISO-8859-1\"");

    assertThat("body is decoded", result.response(), equalTo("Grüße"));
  }

  @Test
  @DisplayName("falls back to UTF-8 when the charset is not supported")
  public void fallsBackToUtf8ForUnsupportedCharset() {
    JavaNetHttpResult result = resultOf("Grüße".getBytes(StandardCharsets.UTF_8), "text/plain; charset=no-such-charset");

    assertThat("body is decoded", result.response(), equalTo("Grüße"));
  }

  @Test
  @DisplayName("falls back to UTF-8 when the charset name is illegal")
  public void fallsBackToUtf8ForIllegalCharsetName() {
    JavaNetHttpResult result = resultOf("Grüße".getBytes(StandardCharsets.UTF_8), "text/plain; charset=not a charset!");

    assertThat("body is decoded", result.response(), equalTo("Grüße"));
  }

  @Test
  @DisplayName("falls back to UTF-8 when the charset parameter has no value")
  public void fallsBackToUtf8ForEmptyCharsetValue() {
    JavaNetHttpResult result = resultOf("Grüße".getBytes(StandardCharsets.UTF_8), "text/plain; charset=");

    assertThat("body is decoded", result.response(), equalTo("Grüße"));
  }

  @Test
  @DisplayName("decodes the body only once")
  public void decodesTheBodyOnlyOnce() {
    JavaNetHttpResult result = resultOf("a body".getBytes(StandardCharsets.UTF_8), "text/plain");

    assertThat("the same string is returned", result.response(), sameInstance(result.response()));
  }

  @Test
  @DisplayName("renders the decoded body into its string representation")
  public void rendersDecodedBodyIntoToString() {
    JavaNetHttpResult result = resultOf("Grüße".getBytes(StandardCharsets.UTF_8), "text/plain; charset=UTF-8");

    assertThat("body is rendered", result.toString(), containsString("ResponseBody: Grüße"));
    assertThat("status code is rendered", result.toString(), containsString("StatusCode: 200"));
  }

  @Test
  @DisplayName("is not implemented by every HttpResult, so responseBytes can be empty")
  public void responseBytesIsEmptyForResultsThatDoNotKeepThem() {
    HcHttpResult result = HcHttpResult.response("a body");

    assertThat("no bytes are kept", result.responseBytes(), equalTo(Option.none()));
    assertThat("the string body is still there", result.response(), equalTo("a body"));
  }
}
