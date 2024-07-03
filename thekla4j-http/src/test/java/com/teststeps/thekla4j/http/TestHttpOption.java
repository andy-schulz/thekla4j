package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.http.spp.HttpOptions;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestHttpOption {

  /**
   * Test setting a query parameter as an option
   */

  @Test
  public void setQueryParameterAsOption() {
    HttpOptions httpOption = HttpOptions.empty()
        .queryParameter("queryParam", Option.of("query param value"));

    assertThat(httpOption.queryParameters.get("queryParam"), equalTo("query param value"));
  }

  @Test
  public void setQueryParameterAsString() {
    HttpOptions httpOption = HttpOptions.empty()
        .queryParameter("queryParam", "query param value");

    assertThat(httpOption.queryParameters.get("queryParam"), equalTo("query param value"));
  }

  @Test
  public void setQueryParameterAsOptionNone() {
    HttpOptions httpOption = HttpOptions.empty()
        .queryParameter("queryParam", Option.none());

    assertThat("no query parameter is set when option is null",
        !httpOption.queryParameters.containsKey("queryParam"));
  }

  /**
   * Test setting a path parameter as a string
   */
  @Test
  public void setPathParameterAsString() {
    HttpOptions httpOption = HttpOptions.empty()
        .pathParameter("pathParam", "pathParamValue");

    assertThat(httpOption.pathParameters.get("pathParam"), equalTo("pathParamValue"));
  }


  /**
   * Test setting a form parameter
   */
  @Test
  public void setFormParameterAsOption() {
    HttpOptions httpOption = HttpOptions.empty()
        .formParameter("formParam", Option.of("formParamValue"));

    assertThat(httpOption.formParameters.get("formParam"), equalTo("formParamValue"));
  }

  @Test
  public void setFormParameterAsString() {
    HttpOptions httpOption = HttpOptions.empty()
        .formParameter("formParam", "formParamValue");

    assertThat(httpOption.formParameters.get("formParam"), equalTo("formParamValue"));
  }

  @Test
  public void setFormParameterAsOptionNone() {
    HttpOptions httpOption = HttpOptions.empty()
        .formParameter("formParam", Option.none());

    assertThat("no form parameter is set when option is null",
        !httpOption.formParameters.containsKey("formParam"));
  }
}
