package com.teststeps.thekla4j.utils.url;

import io.vavr.Function1;
import io.vavr.control.Try;
import java.net.URL;
import java.util.Map;

/**
 * Utility class providing URL helper functions.
 */
public class UrlHelper {

  /**
   * Sanitizes a URL by masking user-info credentials in the authority component.
   */
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

  /**
   * Builds a URL query string from the given parameter map.
   * Returns an empty string if the map is null or empty.
   */
  public static final Function1<Map<String, String>, String> buildQueryString = params -> {

    System.out.println(params);

    if (params == null || params.isEmpty()) {
      return "";
    }

    return "?" + io.vavr.collection.HashMap.ofAll(params)
        .map(t -> t._1 + "=" + t._2)
        .mkString("&");
  };
}
