package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.http.spp.HttpOptions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestRedirectOptions {

  @Test
  public void setRedirectInOptions() {

    HttpOptions options = HttpOptions.empty()
        .followRedirects(false);

    assertThat(options.followRedirects, equalTo(false));

  }

  @Test
  public void defaultRedirectIsTrue() {

    HttpOptions options = HttpOptions.empty();

    assertThat(options.followRedirects, equalTo(true));

  }

  @Test
  public void mergeTrueOnFalseShouldStayFalse() {

    HttpOptions redirectOptsFalse = HttpOptions.empty()
        .followRedirects(false);

    HttpOptions redirectOptsTrue = HttpOptions.empty();

    HttpOptions mergeTrueOnTopOfFalse = redirectOptsTrue.mergeOnTopOf(redirectOptsFalse);

    assertThat(mergeTrueOnTopOfFalse.followRedirects, equalTo(false));


  }

  @Test
  public void mergeFalseOnTrueShouldStayFalse() {

    HttpOptions redirectOptsFalse = HttpOptions.empty()
        .followRedirects(false);

    HttpOptions redirectOptsTrue = HttpOptions.empty();

    HttpOptions mergeTrueOnTopOfFalse = redirectOptsFalse.mergeOnTopOf(redirectOptsTrue);

    assertThat(mergeTrueOnTopOfFalse.followRedirects, equalTo(false));
  }

  @Test
  public void mergeTrueOnTrueShouldBeTrue() {

    HttpOptions redirectOptsTrue = HttpOptions.empty();

    HttpOptions redirectOptsTrue1 = HttpOptions.empty();

    HttpOptions mergeTrueOnTrue = redirectOptsTrue.mergeOnTopOf(redirectOptsTrue1);

    assertThat(mergeTrueOnTrue.followRedirects, equalTo(true));
  }

  @Test
  public void mergeFalseOnFalseShouldBeFalse() {

    HttpOptions redirectOptsFalse = HttpOptions.empty().followRedirects(false);

    HttpOptions redirectOptsFalse1 = HttpOptions.empty().followRedirects(false);

    HttpOptions mergeFalseOnFalse = redirectOptsFalse.mergeOnTopOf(redirectOptsFalse1);

    assertThat(mergeFalseOnFalse.followRedirects, equalTo(false));
  }



}
