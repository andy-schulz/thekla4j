package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.http.spp.AcceptEncoding;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestHeaderValuesAcceptEncoding {

  @Test
  @DisplayName("test accept encoding header values")
  void testAcceptEncodingHeaderValues() {
    assertThat("assert GZIP set",  AcceptEncoding.GZIP.asString(), equalTo("gzip"));
    assertThat("assert DEFLATE set",  AcceptEncoding.DEFLATE.asString(), equalTo("deflate"));
    assertThat("assert BR set",  AcceptEncoding.BR.asString(), equalTo("br"));
    assertThat("assert IDENTITY set",  AcceptEncoding.IDENTITY.asString(), equalTo("identity"));
    assertThat("assert COMPRESS set",  AcceptEncoding.COMPRESS.asString(), equalTo("compress"));
  }
}
