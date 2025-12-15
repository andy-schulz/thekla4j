package com.teststeps.thekla4j.http.httpRequest;


import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import io.vavr.control.Either;
import java.net.http.HttpClient;
import java.security.cert.X509Certificate;
import java.util.function.Function;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "JavaNetHttpClient")
public class JavaNetHttpClient implements com.teststeps.thekla4j.http.core.HttpClient {
  private final HttpClient.Builder httpClientBuilder;
  private final HttpOptions clientHttpOptions;

  public static JavaNetHttpClient using(HttpOptions opts) {
    return new JavaNetHttpClient(opts);
  }

  private JavaNetHttpClient(HttpOptions opts) {
    this.httpClientBuilder = HttpClient.newBuilder();
    this.clientHttpOptions = opts;
  }

  @Override
  public Either<Throwable, com.teststeps.thekla4j.http.core.HttpResult> send(Request request, HttpOptions activityOptions, Function<com.teststeps.thekla4j.http.core.HttpRequest, Either<Throwable, HttpResult>> method) {

    HttpOptions options = HttpOptions.empty()
        .mergeOnTopOf(this.clientHttpOptions)
        .mergeOnTopOf(activityOptions)
        .mergeOnTopOf(request.options);

    if (options.getFollowRedirects()) {
      httpClientBuilder.followRedirects(HttpClient.Redirect.NORMAL);
    } else {
      httpClientBuilder.followRedirects(HttpClient.Redirect.NEVER);
    }

    if (options.getDisableSSLCertificateValidation()) {
      try {
        httpClientBuilder.sslContext(insecureContext());
      } catch (Exception e) {
        return Either.left(e);
      }
    }

    httpClientBuilder.connectTimeout(options.getResponseTimeout());

    com.teststeps.thekla4j.http.core.HttpRequest req = JavaNetHttpRequest.on(request.resource)
        .doing(request.description)
        .using(options)
        .executeWith(httpClientBuilder.build());

    return method.apply(req);
  }

  private SSLContext insecureContext() throws Exception {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
    return sslContext;
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
