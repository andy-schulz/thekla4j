package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.http.spp.Authorization;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestHeaderValuesAuthorization {

  @Test
  @DisplayName("test authorization header values")
  void testAuthorizationHeaderValues() {

    assertThat("Basic Auth is set", Authorization.BASIC.of("MYAUTH").asString(), equalTo("Basic MYAUTH"));
    assertThat("Bearer Auth is set", Authorization.BEARER.of("MYAUTH").asString(), equalTo("Bearer MYAUTH"));
  }
}
