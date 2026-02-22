package com.teststeps.thekla4j.http.httpRequest;


import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.core.HttpVersion;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.net.http.HttpClient;
import java.security.cert.X509Certificate;
import java.util.function.Function;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.log4j.Log4j2;

/**
 * Java {@link java.net.http.HttpClient} based implementation of the {@link com.teststeps.thekla4j.http.core.HttpClient}
 * interface.
 */
@Log4j2(topic = "JavaNetHttpClient")
public class JavaNetHttpClient implements com.teststeps.thekla4j.http.core.HttpClient {
  private final HttpClient.Builder httpClientBuilder;
  private final HttpOptions clientHttpOptions;

  /**
   * Creates a new {@code JavaNetHttpClient} with the specified base options.
   * 
   * @param opts the HTTP options to use as client-level defaults
   * @return a new JavaNetHttpClient instance
   */
  public static JavaNetHttpClient using(HttpOptions opts) {
    return new JavaNetHttpClient(opts);
  }

  private JavaNetHttpClient(HttpOptions opts) {
    this.httpClientBuilder = HttpClient.newBuilder();
    this.clientHttpOptions = opts;
  }

  @Override
  public Either<ActivityError, com.teststeps.thekla4j.http.core.HttpResult> send(Request request, HttpOptions activityOptions, Function<com.teststeps.thekla4j.http.core.HttpRequest, Try<HttpResult>> method) {

    HttpOptions options = HttpOptions.empty()
        .mergeOnTopOf(this.clientHttpOptions)
        .mergeOnTopOf(activityOptions)
        .mergeOnTopOf(request.options);

    if (options.getFollowRedirects()) {
      httpClientBuilder.followRedirects(HttpClient.Redirect.NORMAL);
    } else {
      httpClientBuilder.followRedirects(HttpClient.Redirect.NEVER);
    }

    // Set HTTP version, defaults to HTTP/1.1
    httpClientBuilder.version(toClientVersion(options.getHttpVersion()));

    if (options.getDisableSSLCertificateValidation()) {
      try {
        httpClientBuilder.sslContext(insecureContext());
      } catch (Exception e) {
        return Either.left(ActivityError.of(e));
      }
    }

    httpClientBuilder.connectTimeout(options.getResponseTimeout());

    com.teststeps.thekla4j.http.core.HttpRequest req = JavaNetHttpRequest.on(request.resource)
        .doing(request.description)
        .using(options)
        .executeWith(httpClientBuilder.build());

    return method.apply(req)
        .transform(ActivityError.toEither("Error sending HTTP request"));
  }

  private SSLContext insecureContext() throws Exception {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
    return sslContext;
  }

  /**
   * Converts HttpVersion enum to java.net.http.HttpClient.Version
   * 
   * @param version the HttpVersion to convert
   * @return the corresponding HttpClient.Version
   * @throws UnsupportedOperationException if the version is not supported by java.net.http.HttpClient
   */
  private HttpClient.Version toClientVersion(HttpVersion version) {
    return switch (version) {
      case HTTP_1_1 -> HttpClient.Version.HTTP_1_1;
      case HTTP_2 -> HttpClient.Version.HTTP_2;
      case HTTP_1_0, HTTP_3 -> throw new UnsupportedOperationException(
                                                                       "HTTP version " + version.getVersion() +
                                                                           " is not supported by java.net.http.HttpClient");
    };
  }

  private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[0];
    }

    public void checkClientTrusted(
                                   java.security.cert.X509Certificate[] certs, String authType) {
    }

    public void checkServerTrusted(
                                   java.security.cert.X509Certificate[] certs, String authType) {
    }
  }
  };

  @Override
  public void destroy() {
    // Nothing to destroy for JavaNetHttpClient
  }
}
