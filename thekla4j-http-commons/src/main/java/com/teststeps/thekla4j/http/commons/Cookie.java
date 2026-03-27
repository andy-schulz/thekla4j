package com.teststeps.thekla4j.http.commons;

import com.teststeps.thekla4j.utils.json.JSON;
import java.time.LocalDateTime;
import lombok.With;

/**
 * Represents an HTTP cookie with all standard attributes.
 */
@With
public record Cookie(
                     /**
                      * the cookie name
                      * 
                      * @param name the cookie name
                      * @return the cookie name
                      */
                     String name,
                     /**
                      * the cookie value
                      * 
                      * @param value the cookie value
                      * @return the cookie value
                      */
                     String value,
                     /**
                      * the domain scope of the cookie, or null for all domains
                      * 
                      * @param domain the domain scope
                      * @return the domain scope
                      */
                     String domain,
                     /**
                      * the path scope of the cookie, or null for all paths
                      * 
                      * @param path the path scope
                      * @return the path scope
                      */
                     String path,
                     /**
                      * the expiry timestamp of the cookie, or null if session-scoped
                      * 
                      * @param expires the expiry timestamp
                      * @return the expiry timestamp
                      */
                     LocalDateTime expires,
                     /**
                      * the SameSite policy (Strict, Lax, None), or null
                      * 
                      * @param sameSite the SameSite policy
                      * @return the SameSite policy
                      */
                     String sameSite,
                     /**
                      * the max-age in seconds, or null
                      * 
                      * @param maxAge the max-age
                      * @return the max-age
                      */
                     String maxAge,
                     /**
                      * whether the cookie is HTTP-only
                      * 
                      * @param httpOnly true if HTTP-only
                      * @return true if HTTP-only
                      */
                     boolean httpOnly,
                     /**
                      * whether the cookie requires HTTPS
                      * 
                      * @param secure true if HTTPS is required
                      * @return true if HTTPS is required
                      */
                     boolean secure,
                     /**
                      * whether the cookie is partitioned (CHIPS)
                      * 
                      * @param partitioned true if partitioned
                      * @return true if partitioned
                      */
                     boolean partitioned
) {

  /**
   * Creates an empty {@code Cookie} with all fields set to their defaults (empty strings / nulls / false).
   *
   * @return an empty {@code Cookie} instance
   */
  public static Cookie empty() {
    return new Cookie(
                      "",
                      "",
                      null,
                      null,
                      null,
                      null,
                      null,
                      false,
                      false,
                      false);
  }

  /**
   * Creates a {@code Cookie} with the given name and value, all other attributes set to their defaults.
   *
   * @param name  the cookie name
   * @param value the cookie value
   * @return a new {@code Cookie} instance
   */
  public static Cookie of(String name, String value) {
    return new Cookie(
                      name,
                      value,
                      null,
                      null,
                      null,
                      null,
                      null,
                      false,
                      false,
                      false);

  }

  public String toString() {
    return JSON.logOf(this);
  }
}
