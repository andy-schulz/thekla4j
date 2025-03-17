package com.teststeps.thekla4j.utils.url;

import io.vavr.Function1;
import io.vavr.control.Try;

import java.net.URL;

public class UrlHelper {

  public static final Function1<String, Try<String>> sanitizeUrl =
    urlString -> Try.of(() -> new URL(urlString))
      .mapTry(url -> {
        String userInfo = url.getUserInfo();
        if (userInfo != null) {
          String sanitizedUserInfo = userInfo.replaceAll(".*", "*");
          return new URL(url.getProtocol(), sanitizedUserInfo + "@" + url.getHost(), url.getPort(), url.getFile()).toString();
        }
        return urlString;
      });
}
