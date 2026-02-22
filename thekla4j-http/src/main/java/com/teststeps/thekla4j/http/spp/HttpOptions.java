package com.teststeps.thekla4j.http.spp;

import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.http.core.HttpVersion;
import com.teststeps.thekla4j.http.core.functions.CookieFunctions;
import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.NonNull;

/**
 * Configuration options for HTTP requests.
 */
public class HttpOptions {

  /** Default timeout duration for HTTP requests */
  public final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);
  /** Default setting for following redirects */
  public final boolean DEFAULT_FOLLOW_REDIRECTS = true;
  /** Default setting for SSL certificate validation */
  public final boolean DEFAULT_DISABLE_SSL_CERTIFICATE_VALIDATION = false;

  /** HTTP headers to send with the request */
  public Map<String, String> headers = new HashMap<>();
  /** Query parameters to append to the URL */
  public Map<String, String> queryParameters = new HashMap<>();
  /** Path parameters to substitute in the URL */
  public Map<String, String> pathParameters = new HashMap<>();
  /** Form parameters for form-encoded requests */
  public Map<String, String> formParameters = new HashMap<>();
  /** Port number for the request */
  public int port = 0;
  /** Base URL for the request */
  public String baseUrl = "";
  /** Request body content */
  public String body = "";

  private Boolean disableSSLCertificateValidation = null;
  private Boolean followRedirects = null;
  private Option<HttpVersion> httpVersion = Option.none();

  /**
   * Default timeout to receive a response from server. Overwrite this value for long running requests where needed,
   * calling responseTimeout() setter
   */
  private Duration responseTimeout = Duration.ZERO;

  /**
   * Getters
   * 
   * @return int value of responseTimeout
   */
  public Duration getResponseTimeout() {
    return responseTimeout == Duration.ZERO ? DEFAULT_TIMEOUT : responseTimeout;
  }

  /**
   * Getters
   * 
   * @return boolean value of followRedirects
   */
  public boolean getFollowRedirects() {
    return followRedirects == null ? DEFAULT_FOLLOW_REDIRECTS : followRedirects;
  }

  /**
   * Getters
   * 
   * @return boolean value of disableSSLCertificateValidation
   */
  public boolean getDisableSSLCertificateValidation() {
    return disableSSLCertificateValidation == null ? DEFAULT_DISABLE_SSL_CERTIFICATE_VALIDATION : disableSSLCertificateValidation;
  }

  /**
   * Getters
   *
   * @return HttpVersion value, defaults to HTTP_1_1 if not set
   */
  public HttpVersion getHttpVersion() {
    return httpVersion.getOrElse(HttpVersion.HTTP_1_1);
  }


  /**
   * Sets the base URL for the HTTP request.
   * 
   * @param baseUrl the base URL
   * @return new HttpOptions instance with the base URL set
   */
  public HttpOptions baseUrl(@NonNull String baseUrl) {
    return getNewRestOptions()
        .setBaseUrl(baseUrl);
  }

  /**
   * Sets the base URL for the HTTP request from an Option.
   * 
   * @param baseUrl the base URL option
   * @return new HttpOptions instance with the base URL set
   */
  public HttpOptions baseUrl(@NonNull Option<String> baseUrl) {
    return getNewRestOptions()
        .setBaseUrl(baseUrl.getOrElse("base_url_is_empty"));
  }

  /**
   * Sets the request body.
   * 
   * @param body the request body content
   * @return new HttpOptions instance with the body set
   */
  public HttpOptions body(String body) {
    return body(Option.of(body));
  }

  /**
   * Sets the request body from an Option.
   * 
   * @param body the request body option
   * @return new HttpOptions instance with the body set
   */
  public HttpOptions body(@NonNull Option<String> body) {
    return getNewRestOptions()
        .setBody(body.getOrElse(""));
  }

  /**
   * Sets a header by name and string value.
   * 
   * @param headerName  the header name
   * @param headerValue the header value
   * @return new HttpOptions instance with the header set
   */
  public HttpOptions header(@NonNull String headerName, @NonNull String headerValue) {
    return getNewRestOptions()
        .setHeaderValue(headerName, headerValue);
  }

  /**
   * Sets a header using a typed header type and value.
   * 
   * @param headerType  the header type
   * @param headerValue the header value
   * @return new HttpOptions instance with the header set
   */
  public HttpOptions header(@NonNull HttpHeaderType headerType, @NonNull HttpHeaderValue headerValue) {
    return header(headerType.asString, headerValue.asString());
  }

  /**
   * Sets a header by name from an optional value.
   * 
   * @param headerName  the header name
   * @param headerValue the optional header value; if empty the options are returned unchanged
   * @param <T>         the value type
   * @return new HttpOptions instance with the header set, or unchanged if option is empty
   */
  public <T> HttpOptions header(@NonNull String headerName, @NonNull Option<T> headerValue) {
    return headerValue
        .map(v -> getNewRestOptions()
            .setHeaderValue(headerName, v instanceof HttpHeaderValue ? ((HttpHeaderValue) v).asString() : Objects.toString(v)))
        .getOrElse(this);
  }

  /**
   * Sets the "Cookie" header from a list of Cookie objects.
   * 
   * @param cookies the list of cookies to include in the request
   * @return new HttpOptions instance with the "Cookie" header set
   */
  public HttpOptions cookies(List<Cookie> cookies) {
    return header("Cookie", CookieFunctions.toCookieStringList.apply(cookies));
  }

  /**
   * Sets a typed header from an optional value.
   * 
   * @param headerType  the header type
   * @param headerValue the optional header value; if empty the options are returned unchanged
   * @param <T>         the value type
   * @return new HttpOptions instance with the header set, or unchanged if option is empty
   */
  public <T> HttpOptions header(@NonNull HttpHeaderType headerType, @NonNull Option<T> headerValue) {
    return header(headerType.asString, headerValue);
  }

  /**
   * Sets multiple headers at once.
   * 
   * @param headers map of header name to header value
   * @return new HttpOptions instance with all headers set
   */
  public HttpOptions headers(@NonNull Map<String, String> headers) {

    HttpOptions opts = getNewRestOptions();

    io.vavr.collection.HashMap<String, String> map = io.vavr.collection.HashMap.ofAll(headers);

    map.foldLeft(opts, (o, tuple2) -> tuple2._2() != null ?
        o.setHeaderValue(tuple2._1(), tuple2._2()) :
        o);

    return opts;
  }

  /**
   * Sets a query parameter from an optional value.
   * 
   * @param queryParameterName  the query parameter name
   * @param queryParameterValue the optional query parameter value
   * @param <T>                 the value type
   * @return new HttpOptions instance with the query parameter set, or unchanged if option is empty
   */
  public <T> HttpOptions queryParameter(@NonNull String queryParameterName, @NonNull Option<T> queryParameterValue) {
    return queryParameterValue
        .map(val -> getNewRestOptions()
            .setParameterValue(queryParameterName, Objects.toString(val)))
        .getOrElse(this);
  }

  /**
   * Sets a query parameter.
   * 
   * @param queryParameterName  the query parameter name
   * @param queryParameterValue the query parameter value
   * @return new HttpOptions instance with the query parameter set
   */
  public HttpOptions queryParameter(@NonNull String queryParameterName, @NonNull String queryParameterValue) {
    return getNewRestOptions()
        .setParameterValue(queryParameterName, queryParameterValue);
  }

  /**
   * Sets a path parameter to substitute in the URL template.
   * 
   * @param pathParameterName  the placeholder name in the URL
   * @param pathParameterValue the value to substitute
   * @return new HttpOptions instance with the path parameter set
   */
  public HttpOptions pathParameter(@NonNull String pathParameterName, @NonNull String pathParameterValue) {
    return getNewRestOptions()
        .setPathPropertyValues(pathParameterName, pathParameterValue);
  }

  /**
   * Sets a form parameter for application/x-www-form-urlencoded requests.
   * 
   * @param formParameterName  the form field name
   * @param formParameterValue the form field value
   * @return new HttpOptions instance with the form parameter set
   */
  public HttpOptions formParameter(@NonNull String formParameterName, @NonNull String formParameterValue) {
    return getNewRestOptions()
        .setFormPropertyValues(formParameterName, formParameterValue);
  }

  /**
   * Sets a form parameter from an optional value.
   * 
   * @param formParameterName  the form field name
   * @param formParameterValue the optional form field value
   * @return new HttpOptions instance with the form parameter set, or unchanged if option is empty
   */
  public HttpOptions formParameter(@NonNull String formParameterName, @NonNull Option<String> formParameterValue) {
    return formParameterValue
        .map(val -> getNewRestOptions()
            .setFormPropertyValues(formParameterName, Objects.toString(val)))
        .getOrElse(this);
  }

  /**
   * Sets the port number for the request.
   * 
   * @param port the port number
   * @return new HttpOptions instance with the port set
   */
  public HttpOptions port(int port) {
    return getNewRestOptions()
        .setPort(port);
  }

  /**
   * Sets the port number from an optional value.
   * 
   * @param port the optional port number
   * @return new HttpOptions instance with the port set, or unchanged if option is empty
   */
  public HttpOptions port(Option<Integer> port) {
    return port.map(p -> getNewRestOptions().setPort(p))
        .getOrElse(this);
  }

  /**
   * Disables or enables SSL certificate validation.
   * 
   * @param disable true to disable SSL certificate validation, false to enable
   * @return new HttpOptions instance with the SSL validation setting
   */
  public HttpOptions disableSSLCertificateValidation(boolean disable) {
    return getNewRestOptions()
        .setDisableSSLCertificateValidation(disable);
  }

  /**
   * Sets whether to follow HTTP redirects.
   * 
   * @param followRedirects true to follow redirects, false to stop at the first redirect response
   * @return new HttpOptions instance with the follow-redirects setting
   */
  public HttpOptions followRedirects(boolean followRedirects) {
    return getNewRestOptions()
        .setFollowRedirects(followRedirects);
  }

  /**
   * Sets the response timeout using a Duration.
   * 
   * @param timeOut the response timeout duration
   * @return new HttpOptions instance with the timeout set
   */
  public HttpOptions responseTimeout(Duration timeOut) {
    return getNewRestOptions()
        .setResponseTimeout(timeOut);
  }

  /**
   * Sets the response timeout in milliseconds.
   * 
   * @param timeOut the response timeout in milliseconds
   * @return new HttpOptions instance with the timeout set
   */
  public HttpOptions responseTimeout(int timeOut) {
    return getNewRestOptions()
        .setResponseTimeout(timeOut);
  }

  /**
   * Sets the HTTP protocol version to use for the request.
   * 
   * @param version the HTTP version
   * @return new HttpOptions instance with the HTTP version set
   */
  public HttpOptions useVersion(HttpVersion version) {
    return getNewRestOptions()
        .setHttpVersion(version);
  }

  /**
   * Creates a deep copy of these HTTP options.
   * 
   * @return a new HttpOptions instance with the same values
   */
  public HttpOptions clone() {
    return getNewRestOptions();
  }

  /**
   * Merges these options on top of the given base options.
   * Values set in this instance override those in {@code mergedOpts}.
   * 
   * @param mergedOpts the base options to merge on top of
   * @return a new HttpOptions instance combining both option sets
   */
  public HttpOptions mergeOnTopOf(HttpOptions mergedOpts) {

    HttpOptions clone = mergedOpts.clone();

    if (!this.baseUrl.isEmpty())
      clone.setBaseUrl(this.baseUrl);

    if (!this.body.isEmpty())
      clone.setBody(this.body);

    if (this.port > 0)
      clone.setPort(this.port);

    if (!Objects.isNull(this.disableSSLCertificateValidation))
      clone.setDisableSSLCertificateValidation(this.disableSSLCertificateValidation);

    if (!Objects.isNull(this.followRedirects)) {
      clone.followRedirects = this.followRedirects;
    }

    if (!this.httpVersion.isEmpty()) {
      clone.httpVersion = this.httpVersion;
    }

    if (this.responseTimeout != Duration.ZERO) {
      clone.setResponseTimeout(this.responseTimeout);
    }


    this.headers.forEach(clone::setHeaderValue);
    clone.dropNullHeader();

    this.queryParameters.forEach(clone::setParameterValue);

    this.pathParameters.forEach(clone::setPathPropertyValues);

    this.formParameters.forEach(clone::setFormPropertyValues);

    return clone;
  }

  /**
   * Privates
   */

  private HttpOptions getNewRestOptions() {
    return new HttpOptions(
                           this.headers, this.queryParameters, this.pathParameters, this.formParameters, this.baseUrl, this.port, this.body,
                           this.disableSSLCertificateValidation, this.responseTimeout, this.followRedirects, this.httpVersion);
  }

  private HttpOptions setBody(String body) {
    this.body = body;
    return this;
  }

  private HttpOptions setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  private HttpOptions setParameterValue(String parameterName, String parameterValue) {
    return this.setMapValues(this.queryParameters, parameterName, parameterValue);
  }

  private HttpOptions setHeaderValue(String headerName, String headerValue) {
    return this.setMapValues(this.headers, headerName, headerValue);
  }

  private HttpOptions dropNullHeader() {
    this.headers = this.headers.entrySet()
        .stream()
        .filter(entry -> entry.getValue() != null)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return this;
  }

  private HttpOptions setPathPropertyValues(String propertyName, String propertyValue) {
    return this.setMapValues(this.pathParameters, propertyName, propertyValue);
  }

  private HttpOptions setFormPropertyValues(String propertyName, String propertyValue) {
    return this.setMapValues(this.formParameters, propertyName, propertyValue);
  }

  private HttpOptions setPort(int port) {
    this.port = port;
    return this;
  }

  private HttpOptions setDisableSSLCertificateValidation(boolean disable) {
    this.disableSSLCertificateValidation = disable;
    return this;
  }

  private HttpOptions setFollowRedirects(boolean followRedirects) {
    this.followRedirects = followRedirects;
    return this;
  }

  private HttpOptions setHttpVersion(HttpVersion version) {
    this.httpVersion = Option.of(version);
    return this;
  }

  private HttpOptions setResponseTimeout(int timeOutInMillis) {
    this.responseTimeout = Duration.ofMillis(timeOutInMillis);
    return this;
  }

  private HttpOptions setResponseTimeout(Duration timeOut) {
    this.responseTimeout = timeOut;
    return this;
  }

  private HttpOptions setMapValues(Map<String, String> map, String mapItemName, String mapItemValue) {

    if (map.containsKey(mapItemName)) {
      map.replace(mapItemName, mapItemValue);
    } else {
      map.put(mapItemName, mapItemValue);
    }
    return this;
  }

  public String toString() {
    return toString(0);
  }

  /**
   * Returns a formatted string representation of these options.
   * 
   * @param indent the indentation level (number of tabs)
   * @return the formatted string
   */
  public String toString(int indent) {
    String indStr = "\n" + String.join("", Collections.nCopies(indent, "\t"));

    return indStr + "BaseUrl: " + this.baseUrl +
        indStr + "Resource Port: " + this.port +
        indStr + "Path Parameters: " + this.pathParameters.toString() +
        indStr + "Query Parameters: " + this.queryParameters.toString() +
        indStr + "Form Parameters: " + this.formParameters.toString() +
        indStr + "Headers: " + this.headers.toString() +
        indStr + "Body: " + this.body.replaceAll("\n", indStr);
  }

  /**
   * constructors
   */

  /**
   * Creates an empty HttpOptions instance with default values.
   * 
   * @return a new empty HttpOptions instance
   */
  public static HttpOptions empty() {
    return new HttpOptions();
  }

  private HttpOptions(
                      Map<String, String> headers, Map<String, String> queryParameters, Map<String, String> pathParameters, Map<String, String> formParameters, String baseUrl, int port, String body, Boolean disableSSLCertificateValidation, Duration responseTimeout, Boolean followRedirects, Option<HttpVersion> httpVersion
  ) {
    // clone fields of request

    this.baseUrl = baseUrl;
    this.body = body;
    this.port = port;
    this.responseTimeout = responseTimeout;
    this.disableSSLCertificateValidation = disableSSLCertificateValidation;
    this.followRedirects = followRedirects;
    this.httpVersion = httpVersion;


    // deep clone??
    String headersJson = JSON.valueAsString(headers).get();
    this.headers = JSON.stringToValue(headersJson, HashMap.class).get();

    String parametersJson = JSON.valueAsString(queryParameters).get();
    this.queryParameters = JSON.stringToValue(parametersJson, HashMap.class).get();

    String propertiesJson = JSON.valueAsString(pathParameters).get();
    this.pathParameters = JSON.stringToValue(propertiesJson, HashMap.class).get();

    String formJson = JSON.valueAsString(formParameters).get();
    this.formParameters = JSON.stringToValue(formJson, HashMap.class).get();
  }

  private HttpOptions() {

  }


}
