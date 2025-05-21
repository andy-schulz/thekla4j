package com.teststeps.thekla4j.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.http.spp.Accept;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestHeaderValuesAccept {


  @Test
  @DisplayName("test accept header values")
  void testAcceptHeaderValues() {

    assertThat("assert EDI_X12 set", Accept.APPLICATION_EDI_X12.asString(), equalTo("application/EDI-X12"));
    assertThat("assert EDIFACT set", Accept.APPLICATION_EDIFACT.asString(), equalTo("application/EDIFACT"));
    assertThat("assert JSON set", Accept.APPLICATION_JSON.asString(), equalTo("application/json"));
    assertThat("assert JAVA_ARCHIVE set", Accept.APPLICATION_JAVA_ARCHIVE.asString(), equalTo("application/java-archive"));
    assertThat("assert JAVASCRIPT set", Accept.APPLICATION_JAVASCRIPT.asString(), equalTo("application/javascript"));
    assertThat("assert OCTET_STREAM set", Accept.APPLICATION_OCTET_STREAM.asString(), equalTo("application/octet-stream"));
    assertThat("assert OGG set", Accept.APPLICATION_OGG.asString(), equalTo("application/ogg"));
    assertThat("assert PDF set", Accept.APPLICATION_PDF.asString(), equalTo("application/pdf"));
    assertThat("assert XHTML_XML set", Accept.APPLICATION_XHTML_XML.asString(), equalTo("application/xhtml+xml"));
    assertThat("assert XML set", Accept.APPLICATION_XML.asString(), equalTo("application/xml"));
    assertThat("assert X_WWW_FORM_URLENCODED set", Accept.APPLICATION_X_WWW_FORM_URLENCODED.asString(), equalTo("application/x-www-form-urlencoded"));
    assertThat("assert ZIP set", Accept.APPLICATION_ZIP.asString(), equalTo("application/zip"));
    assertThat("assert ID_JSON set", Accept.APPLICATION_ID_JSON.asString(), equalTo("application/ld+json"));

  }
}
