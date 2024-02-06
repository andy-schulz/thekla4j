package com.teststeps.thekla4j.http.httpConn;

import com.teststeps.thekla4j.http.core.Cookie;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.core.functions.CookieFunctions;
import com.teststeps.thekla4j.http.httpConn.functions.ConnectionFunctions;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import io.vavr.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.teststeps.thekla4j.http.httpConn.functions.ConnectionFunctions.encodeParameter;
import static com.teststeps.thekla4j.http.httpConn.functions.ConnectionFunctions.percentEncode;

@Log4j2(topic = "HcHttpRequest")
public class HcHttpRequest implements HttpRequest {

  public static class HCRequestHelper {
    private String resource = "";
    private String description = "";

    public HCRequestHelper doing(String description) {
      this.description = description;
      return this;
    }

    public HttpRequest using(HttpOptions opts) throws Exception {
      return new HcHttpRequest(this.resource, description, opts);
    }

    HCRequestHelper(String resource) {
      this.resource = resource;
    }
  }

  private HttpURLConnection connection;

  private HttpOptions opts;
  private String resource = "";
  private String description = "";

  private final Function1<HttpURLConnection, Try<HttpURLConnection>> writeBody = con -> Try.of(() -> {

    if (Objects.isNull(opts.body) || opts.body.length() == 0) {
      this.connection.setFixedLengthStreamingMode(0);
      log.debug("setFixedLengthStreamingMode: {}", 0);
    }

    if (this.opts.body != null && this.opts.body.length() > 0) {
      log.debug("Writing body: {}", this.opts.body);
      OutputStream os = con.getOutputStream();
      byte[] input = this.opts.body.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }
    return con;
  });

  private final Function3<File, String, HttpURLConnection, Try<HttpURLConnection>> writeFile = (file, fieldName, con) -> Try.of(() -> {

    String boundary = UUID.randomUUID().toString().replace("-", "");
    String crlf = "\r\n";
    String twoHyphens = "--";

    String LINE = crlf;

    con.setRequestMethod("POST");
    con.setRequestProperty("Connection", "Keep-Alive");
    con.setRequestProperty("Cache-Control", "no-cache");
    con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

    DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);

    // add file part
    String fileName = file.getName();

    StringWriter sw = new StringWriter();
    this.appendFormParameters.apply(this.opts, boundary, LINE, sw);

    sw.append(twoHyphens + boundary).append(LINE);
    sw.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE);
    sw.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(fileName)).append(LINE);
    sw.append("Content-Transfer-Encoding: binary").append(LINE);
    sw.append(LINE);

    log.trace(() -> "Content: " + sw);
    writer.append(sw.toString());
    writer.flush();

    FileInputStream inputStream = new FileInputStream(file);
    byte[] buffer = new byte[4096];
    int bytesRead = -1;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }
    outputStream.flush();
    inputStream.close();
    writer.append(LINE);
    writer.flush();

    writer.append(twoHyphens + boundary + twoHyphens).append(LINE);
    writer.close();

    return con;
  });

  private final Function4<HttpOptions, String, String, StringWriter, StringWriter> appendFormParameters = (opts, boundary, LINE, writer) -> {
    HashMap.ofAll(opts.formParameters)
        .forEach((k,v) -> this.appendFormParameter.apply(k, v, boundary, LINE, writer));
    return null;
  };

  private final Function5<String, String, String, String, StringWriter, Void> appendFormParameter = (key, value, boundary, LINE, writer) -> {

    writer.append("--").append(boundary).append(LINE)
        .append("Content-Disposition: form-data; name=\"" + key + "\"")
        .append(LINE)
        .append(LINE)
        .append(value)
        .append(LINE);

    return null;
  };

  private final Function1<HttpURLConnection, Try<HttpURLConnection>> writeFormParameter = con -> Try.of(() -> {

    String formContent = ConnectionFunctions.getFormContent.apply(this.opts);
    log.debug("Writing Form Parameter: {}", formContent);

    byte[] bytes = formContent.getBytes(StandardCharsets.UTF_8);
    con.setRequestProperty("Content-Length", Integer.toString(bytes.length));

    OutputStream os = new DataOutputStream(con.getOutputStream());
    os.write(bytes, 0, bytes.length);

    return con;
  });

  private final Function1<HttpURLConnection, Try<HttpURLConnection>> writeContent =

      con -> ConnectionFunctions.isXWwwFormUrlencoded.apply(this.opts)
          .map(isForm -> isForm ?
              writeFormParameter :
              writeBody)
          .flatMap(func -> func.apply(con));

  public static HCRequestHelper on(String resource) {
    return new HCRequestHelper(resource);
  }

  private Try<HttpURLConnection> executeRequest(String method) {
    return
        Try.of(() -> {
          this.connection.setRequestMethod(method);
          this.connection.setInstanceFollowRedirects(opts.followRedirects);
          return this.connection;
        }).map(con -> {
          con.setDoOutput(true);
          return con;
        });
  }

  @Override
  public Either<Throwable, HttpResult> get() {
    log.debug("Method: GET");

    return this.executeRequest("GET")
        .toEither()
        .flatMap(this::send);
  }

  @Override
  public Either<Throwable, HttpResult> post() {
    log.debug("Method: POST with body of length: {}", Objects.isNull(opts.body) ? -1 : opts.body.length());

    return this.executeRequest("POST")
        .flatMap(this.writeContent)
        .toEither()
        .flatMap(this::send);
//        return t.isSuccess() ? t.map(this::send).get() : Either.left(t.getCause());
  }

  @Override
  public Either<Throwable, HttpResult> postFile(File file, String fieldName) {
    log.debug("Method: POST with body of length: {}", Objects.isNull(opts.body) ? -1 : opts.body.length());

    return this.executeRequest("POST")
        .flatMap(this.writeFile.apply(file, fieldName))
        .toEither()
        .flatMap(this::send);
  }

  @Override
  public Either<Throwable, HttpResult> patch() {
    return null;
  }

  @Override
  public Either<Throwable, HttpResult> put() {
    log.debug("Method: PUT with body of length: {}", Objects.isNull(opts.body) ? -1 : opts.body.length());

    if (Objects.isNull(opts.body)) {
      this.connection.setFixedLengthStreamingMode(0);
      log.debug("setFixedLengthStreamingMode: {}", 0);
    }


    return this.executeRequest("PUT")
        .flatMap(this.writeBody)
        .toEither()
        .flatMap(this::send);

  }

  @Override
  public Either<Throwable, HttpResult> delete() {
    log.debug("Method: DELETE");

    if (Objects.isNull(opts.body))
      this.connection.setFixedLengthStreamingMode(0);

    return this.executeRequest("DELETE")
        .flatMap(this.writeBody)
        .toEither()
        .flatMap(this::send);
  }

  private Try<HttpURLConnection> init(String resource, HttpOptions opts) {
    log.info(() -> "Starting Request: " + this.description);
    String u = getUrl(opts.baseUrl, opts.port, resource, opts.queryParameters, opts.pathParameters);
    log.debug("Using url: {}", u);
    log.debug(() -> "with options: " + opts.toString(1));

    return Try.of(() -> new URL(u))
        .mapTry(url -> (HttpURLConnection) url.openConnection())
        .map(con -> this.setHeaders(con, opts.headers));
  }

  private HttpURLConnection setHeaders(HttpURLConnection con, Map<String, String> headers) {
    headers.forEach(con::setRequestProperty);
    return con;
  }

  private String getUrl(String baseUrl, int port, String resource, Map<String, String> queryParameters, Map<String, String> pathParameters) {
    String url = (baseUrl.length() > 0 ?
        baseUrl.concat(port > 0 ? ":" + port + resource : resource) :
        resource)
        .concat(queryParameters != null && queryParameters.size() > 0 ? "?" : "")
        .concat(getParameterString(queryParameters));

    return pathParameters.entrySet().stream()
        .map(entry -> (Function<String, String>) s -> s.replaceAll(":" + entry.getKey(), entry.getValue()))
        .reduce(Function.identity(), Function::andThen)
        .apply(url);
  }

  private String getParameterString(Map<String, String> params) {
    return params
        .entrySet()
        .stream()
        .map(entry -> entry.getKey() + "=" + percentEncode.apply(entry.getValue()))
        .collect(Collectors.joining("&"));
  }

  private Either<Throwable, HttpResult> send(HttpURLConnection con) {

    Try<HcHttpResult> t = Try.of(() -> {
      log.debug("Response Code: {}", con.getResponseCode());

      BufferedReader br = con.getResponseCode() < 400 ?
          new BufferedReader(
              new InputStreamReader(
                  con.getInputStream() == null ?
                      new ByteArrayInputStream(new byte[0]) :
                      con.getInputStream(),
                  StandardCharsets.UTF_8
              )) :

          new BufferedReader(
              new InputStreamReader(
                  con.getErrorStream() == null ?
                      new ByteArrayInputStream(new byte[0]) :
                      con.getErrorStream(),
                  StandardCharsets.UTF_8
              ));


      StringBuilder response = new StringBuilder();
      String responseLine = null;
      while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
      }

      if (!log.isTraceEnabled())
        log.debug("Response: {}", () -> {
          String resp = response.toString();
          return resp.length() > 1000 ? resp.substring(0, 1000) + "...." : resp;
        });

      log.trace("Response: {}", () -> {
        String resp = response.toString();
        return resp;
      });

      io.vavr.collection.HashMap<String, List<String>> headers =
          HashMap.ofAll(con.getHeaderFields())
              .mapValues(List::ofAll);
      log.debug("Response Headers: {}", () -> headers);

      io.vavr.collection.List<Cookie> cookies =
          headers.filter((k, v) -> Objects.equals(k.toLowerCase(), "set-cookie"))
              .toList()
              .flatMap(tuple -> tuple._2)
              .map(CookieFunctions.toCookie);
      log.debug("Cookies: {}", () -> cookies);


      return HcHttpResult
          .response(response.toString())
          .statusCode(con.getResponseCode())
          .headers(headers)
          .cookies(cookies);
    });

    con.disconnect();

    return t.isSuccess() ?
        Either.right(t.get()) :
        Either.left(t.getCause());

  }

  private void disableSSLCertificateValidation() {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          public void checkClientTrusted(X509Certificate[] certs, String authType) {
          }

          public void checkServerTrusted(X509Certificate[] certs, String authType) {
          }
        }
    };

    // Install the all-trusting trust manager
    SSLContext sc = null;
    try {
      sc = SSLContext.getInstance("SSL");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    try {
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
    } catch (KeyManagementException e) {
      e.printStackTrace();
    }

    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
  }

  public HcHttpRequest(final String resource, final String description, final HttpOptions opts) throws Exception {

    if (opts.disableSSLCertificateValidation)
      disableSSLCertificateValidation();

    this.resource = resource;
    this.description = description;
    this.connection = this.init(resource, opts)
        .getOrElseThrow(ex -> new Exception(ex.toString(), ex));
//                .onSuccess(con -> this.connection = con);
    this.connection.setReadTimeout(opts.responseTimeout);
    this.opts = opts;
  }
}
