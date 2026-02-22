package com.teststeps.thekla4j.http.httpConn;

import static com.teststeps.thekla4j.http.core.functions.UrlFunctions.getFormContent;
import static com.teststeps.thekla4j.http.core.functions.UrlFunctions.getUrl;
import static com.teststeps.thekla4j.http.core.functions.UrlFunctions.isXWwwFormUrlencoded;
import static com.teststeps.thekla4j.http.httpConn.MultipartFunctions.appendFileParts;
import static com.teststeps.thekla4j.http.httpConn.MultipartFunctions.appendParts;

import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.core.functions.CookieFunctions;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import io.vavr.Function1;
import io.vavr.Function3;
import io.vavr.NotImplementedError;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.log4j.Log4j2;

/**
 * Apache HttpComponents implementation of HTTP requests using HttpURLConnection.
 */
@Log4j2(topic = "HcHttpRequest")
public class HcHttpRequest implements HttpRequest {

  /**
   * Helper class for building HTTP requests with fluent API.
   */
  public static class HCRequestHelper {
    private String resource = "";
    private String description = "";

    /**
     * Sets the description for this request.
     * 
     * @param description the request description
     * @return this helper for method chaining
     */
    public HCRequestHelper doing(String description) {
      this.description = description;
      return this;
    }

    /**
     * Creates the HTTP request with the specified options.
     * 
     * @param opts the HTTP options to use
     * @return a new HttpRequest instance
     * @throws Exception if request creation fails
     */
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

    if (Objects.isNull(opts.body) || opts.body.isEmpty()) {
      this.connection.setFixedLengthStreamingMode(0);
      log.debug("setFixedLengthStreamingMode: {}", 0);
    }

    if (this.opts.body != null && !this.opts.body.isEmpty()) {
      log.debug("Writing body: {}", this.opts.body);
      OutputStream os = con.getOutputStream();
      byte[] input = this.opts.body.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }
    return con;
  });

  private final Function3<List<FilePart>, List<Part>, HttpURLConnection, Try<HttpURLConnection>> writeFile =
      (fileParts, parts, con) -> Try.of(() -> {

        String boundary = UUID.randomUUID().toString().replace("-", "");
        String crlf = "\r\n";
        String twoHyphens = "--";

        con.setRequestMethod("POST");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Cache-Control", "no-cache");
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);

        StringWriter sw = new StringWriter();
        sw.append(crlf).append(crlf).append(twoHyphens).append(boundary).append(crlf);

        StringWriter logWriter = new StringWriter();

        return appendParts.apply(parts, boundary, crlf, sw)

            .peek(__ -> {
              printWriter.append(sw.toString());
              logWriter.append(sw.toString());
            })

            .peek(__ -> {
              if (!fileParts.isEmpty() && !parts.isEmpty()) {
                printWriter.append(crlf);
                logWriter.append(crlf);
              }
            })

            .flatMap(__ -> appendFileParts.apply(fileParts, boundary, crlf, printWriter, outputStream, logWriter))
            .andFinally(() -> logWriter.append(twoHyphens))
            .andFinally(() -> printWriter.append(twoHyphens).close())
            .onSuccess(__ -> log.trace(() -> "Multipart writing content: " + logWriter));
      })
          .flatMap(Function.identity())
          .map(__ -> con);


  private final Function1<HttpURLConnection, Try<HttpURLConnection>> writeFormParameter = con -> Try.of(() -> {

    String formContent = getFormContent.apply(this.opts);
    log.debug("Writing Form Parameter: {}", formContent);

    byte[] bytes = formContent.getBytes(StandardCharsets.UTF_8);
    con.setRequestProperty("Content-Length", Integer.toString(bytes.length));

    OutputStream os = new DataOutputStream(con.getOutputStream());
    os.write(bytes, 0, bytes.length);

    return con;
  });

  private final Function1<HttpURLConnection, Try<HttpURLConnection>> writeContent =

      con -> isXWwwFormUrlencoded.apply(this.opts)
          .map(isForm -> isForm ?
              writeFormParameter :
              writeBody)
          .flatMap(func -> func.apply(con));

  /**
   * Creates a new request helper for the specified resource.
   * 
   * @param resource the resource URL or path
   * @return a new HCRequestHelper instance
   */
  public static HCRequestHelper on(String resource) {
    return new HCRequestHelper(resource);
  }

  private Try<HttpURLConnection> executeRequest(String method) {
    return Try.of(() -> {
      this.connection.setRequestMethod(method);
      this.connection.setInstanceFollowRedirects(opts.getFollowRedirects());
      return this.connection;
    }).map(con -> {
      con.setDoOutput(true);
      return con;
    });
  }

  @Override
  public Try<HttpResult> get() {
    log.debug("Method: GET");

    return this.executeRequest("GET")
        .flatMap(this::send);
  }

  @Override
  public Try<HttpResult> post() {
    log.debug("Method: POST with body of length: {}", Objects.isNull(opts.body) ? -1 : opts.body.length());

    return this.executeRequest("POST")
        .flatMap(this.writeContent)
        .flatMap(this::send);
//        return t.isSuccess() ? t.map(this::send).get() : Either.left(t.getCause());
  }

  @Override
  public Try<HttpResult> postFile(List<FilePart> fileParts, List<Part> parts) {
    log.debug("Method: POST of file with body of length: {}", Objects.isNull(opts.body) ? -1 : opts.body.length());

    return this.executeRequest("POST")
        .flatMap(this.writeFile.apply(fileParts, parts))
        .flatMap(this::send);
  }

  @Override
  public Try<HttpResult> patch() {
    return Try.failure(new NotImplementedError("PATCH method is not implemented in HcHttpClient. Please use the JavaNetHttpClient implementation."));
  }

  @Override
  public Try<HttpResult> put() {
    log.debug("Method: PUT with body of length: {}", Objects.isNull(opts.body) ? -1 : opts.body.length());

    if (Objects.isNull(opts.body)) {
      this.connection.setFixedLengthStreamingMode(0);
      log.debug("setFixedLengthStreamingMode: {}", 0);
    }


    return this.executeRequest("PUT")
        .flatMap(this.writeBody)
        .flatMap(this::send);

  }

  @Override
  public Try<HttpResult> delete() {
    log.debug("Method: DELETE");

    if (Objects.isNull(opts.body))
      this.connection.setFixedLengthStreamingMode(0);

    return this.executeRequest("DELETE")
        .flatMap(this.writeBody)
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


  private Try<HttpResult> send(HttpURLConnection con) {

    Try<HcHttpResult> t = Try.of(() -> {
      log.debug("Response Code: {}", con.getResponseCode());

      BufferedReader br = con.getResponseCode() < 400 ?
          new BufferedReader(
                             new InputStreamReader(
                                                   con.getInputStream() == null ?
                                                       new ByteArrayInputStream(new byte[0]) :
                                                       con.getInputStream(),
                                                   StandardCharsets.UTF_8)) :

          new BufferedReader(
                             new InputStreamReader(
                                                   con.getErrorStream() == null ?
                                                       new ByteArrayInputStream(new byte[0]) :
                                                       con.getErrorStream(),
                                                   StandardCharsets.UTF_8));


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
          headers.filter((k, v) -> Objects.equals(Option.of(k).map(String::toLowerCase).getOrNull(), "set-cookie"))
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
        Try.success(t.get()) :
        Try.failure(t.getCause());

  }

  private void disableSSLCertificateValidation() {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
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

  /**
   * Creates a new HTTP request with the specified resource, description, and options.
   * 
   * @param resource    the resource URL or path
   * @param description the description of the request
   * @param opts        the HTTP options to use
   * @throws Exception if initialization or connection fails
   */
  public HcHttpRequest(final String resource, final String description, final HttpOptions opts) throws Exception {

    if (opts.getDisableSSLCertificateValidation())
      disableSSLCertificateValidation();

    this.resource = resource;
    this.description = description;
    this.connection = this.init(resource, opts)
        .getOrElseThrow(ex -> new Exception(ex.toString(), ex));
//                .onSuccess(con -> this.connection = con);
    this.connection.setReadTimeout((int) opts.getResponseTimeout().toMillis());
    this.opts = opts;
  }
}
