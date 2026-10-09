package com.teststeps.thekla4j.http.core;

import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;

/**
 * Interface representing the result of an HTTP request.
 */
public interface HttpResult {
  /**
   * Returns the HTTP status code.
   * 
   * @return the status code
   */
  Integer statusCode();

  /**
   * Returns the response body as a string.
   * <p>
   * The body is decoded using the character set given in the {@code Content-Type} response header,
   * falling back to UTF-8 if the header is absent or names an unsupported character set. Bytes that
   * are not valid in that character set are replaced, so this method must not be used to read binary
   * content - use {@link #responseBytes()} for that.
   * 
   * @return the response body
   */
  String response();

  /**
   * Returns the undecoded response body.
   * <p>
   * This is the authoritative form of the body: {@link #response()} is derived from it. Use it to read
   * binary content such as a downloaded file, where decoding to a string would destroy bytes that are
   * not valid in the response character set.
   * <p>
   * The returned array is the internal one and is not copied, so it must not be modified.
   * <p>
   * Note that no content encoding is applied. If the response carries a {@code Content-Encoding} such
   * as {@code gzip}, the bytes returned are still compressed.
   * 
   * @return the response body as bytes, or {@link Option#none()} if the implementation does not keep them
   */
  default Option<byte[]> responseBytes() {
    return Option.none();
  }

  /**
   * Returns the HTTP response headers.
   * 
   * @return map of header names to header values
   */
  Map<String, List<String>> headers();

  /**
   * Returns the cookies received in the response.
   * 
   * @return list of cookies
   */
  List<Cookie> cookies();

  String toString();

  /**
   * Returns a string representation of the result with indentation.
   * 
   * @param indent the indentation level
   * @return formatted string representation
   */
  String toString(int indent);
}
