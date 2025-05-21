package com.teststeps.thekla4j.http.spp;

import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.http.core.functions.CookieFunctions;
import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.NonNull;

public class HttpOptions {

  public final int DEFAULT_TIMEOUT = 60000;
  public final boolean DEFAULT_FOLLOW_REDIRECTS = true;
  public final boolean DEFAULT_DISABLE_SSL_CERTIFICATE_VALIDATION = false;

  public Map<String, String> headers = new HashMap<>();
  public Map<String, String> queryParameters = new HashMap<>();
  public Map<String, String> pathParameters = new HashMap<>();
  public Map<String, String> formParameters = new HashMap<>();
  public int port = 0;
  public String baseUrl = "";
  public String body = "";

  private Boolean disableSSLCertificateValidation = null;
  private Boolean followRedirects = null;

  /**
   * Default timeout to receive a response from server. Overwrite this value for long running requests where needed,
   * calling responseTimeout() setter
   */
  private int responseTimeout = 0;

  /**
   * Getters
   * 
   * @return int value of responseTimeout
   */
  public int getResponseTimeout() {
    return responseTimeout <= 0 ? DEFAULT_TIMEOUT : responseTimeout;
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


  public HttpOptions baseUrl(@NonNull String baseUrl) {
    return getNewRestOptions()
        .setBaseUrl(baseUrl);
  }

  public HttpOptions baseUrl(@NonNull Option<String> baseUrl) {
    return getNewRestOptions()
        .setBaseUrl(baseUrl.getOrElse("base_url_is_empty"));
  }

  public HttpOptions body(String body) {
    return body(Option.of(body));
  }

  public HttpOptions body(@NonNull Option<String> body) {
    return getNewRestOptions()
        .setBody(body.getOrElse(""));
  }

  public HttpOptions header(@NonNull String headerName, @NonNull String headerValue) {
    return getNewRestOptions()
        .setHeaderValue(headerName, headerValue);
  }

  public HttpOptions header(@NonNull HttpHeaderType headerType, @NonNull HttpHeaderValue headerValue) {
    return header(headerType.asString, headerValue.asString());
  }

  public <T> HttpOptions header(@NonNull String headerName, @NonNull Option<T> headerValue) {
    return headerValue
        .map(v -> getNewRestOptions()
            .setHeaderValue(headerName, v instanceof HttpHeaderValue ? ((HttpHeaderValue) v).asString() : Objects.toString(v)))
        .getOrElse(this);
  }

  public HttpOptions cookies(List<Cookie> cookies) {
    return header("Cookie", CookieFunctions.toCookieStringList.apply(cookies));
  }

  public <T> HttpOptions header(@NonNull HttpHeaderType headerType, @NonNull Option<T> headerValue) {
    return header(headerType.asString, headerValue);
  }

  public HttpOptions headers(@NonNull Map<String, String> headers) {

    HttpOptions opts = getNewRestOptions();

    io.vavr.collection.HashMap<String, String> map = io.vavr.collection.HashMap.ofAll(headers);

    map.foldLeft(opts, (o, tuple2) -> tuple2._2() != null ?
        o.setHeaderValue(tuple2._1(), tuple2._2()) :
        o);

    return opts;
  }

  public <T> HttpOptions queryParameter(@NonNull String queryParameterName, @NonNull Option<T> queryParameterValue) {
    return queryParameterValue
        .map(val -> getNewRestOptions()
            .setParameterValue(queryParameterName, Objects.toString(val)))
        .getOrElse(this);
  }

  public HttpOptions queryParameter(@NonNull String queryParameterName, @NonNull String queryParameterValue) {
    return getNewRestOptions()
        .setParameterValue(queryParameterName, queryParameterValue);
  }

  public HttpOptions pathParameter(@NonNull String pathParameterName, @NonNull String pathParameterValue) {
    return getNewRestOptions()
        .setPathPropertyValues(pathParameterName, pathParameterValue);
  }

  public HttpOptions formParameter(@NonNull String formParameterName, @NonNull String formParameterValue) {
    return getNewRestOptions()
        .setFormPropertyValues(formParameterName, formParameterValue);
  }

  public HttpOptions formParameter(@NonNull String formParameterName, @NonNull Option<String> formParameterValue) {
    return formParameterValue
        .map(val -> getNewRestOptions()
            .setFormPropertyValues(formParameterName, Objects.toString(val)))
        .getOrElse(this);
  }

  public HttpOptions port(int port) {
    return getNewRestOptions()
        .setPort(port);
  }

  public HttpOptions port(Option<Integer> port) {
    return port.map(p -> getNewRestOptions().setPort(p))
        .getOrElse(this);
  }

  public HttpOptions disableSSLCertificateValidation(boolean disable) {
    return getNewRestOptions()
        .setDisableSSLCertificateValidation(disable);
  }

  public HttpOptions followRedirects(boolean followRedirects) {
    return getNewRestOptions()
        .setFollowRedirects(followRedirects);
  }

  public HttpOptions responseTimeout(int timeOut) {
    return getNewRestOptions()
        .setResponseTimeout(timeOut);
  }

  public HttpOptions clone() {
    return getNewRestOptions();
  }

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


    if (this.responseTimeout > 0) {
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
                           this.disableSSLCertificateValidation, this.responseTimeout, this.followRedirects);
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

  private HttpOptions setResponseTimeout(int timeOut) {
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

  public static HttpOptions empty() {
    return new HttpOptions();
  }

  private HttpOptions(
                      Map<String, String> headers, Map<String, String> queryParameters, Map<String, String> pathParameters, Map<String, String> formParameters, String baseUrl, int port, String body, Boolean disableSSLCertificateValidation, int responseTimeout, Boolean followRedirects
  ) {
    // clone fields of request

    this.baseUrl = baseUrl;
    this.body = body;
    this.port = port;
    this.responseTimeout = responseTimeout;
    this.disableSSLCertificateValidation = disableSSLCertificateValidation;
    this.followRedirects = followRedirects;


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
