package com.teststeps.thekla4j.http;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.teststeps.thekla4j.http.spp.HttpOptions;
import org.junit.jupiter.api.Test;

public class TestRedirectOptions {

  @Test
  public void setRedirectInOptions() {

    HttpOptions options = HttpOptions.empty()
        .followRedirects(false);

    assertThat(options.getFollowRedirects(), equalTo(false));

  }

  @Test
  public void defaultRedirectIsTrue() {

    HttpOptions options = HttpOptions.empty();

    assertThat(options.getFollowRedirects(), equalTo(true));

  }

  @Test
  public void mergeTrueOnFalseShouldStayFalse() {

    HttpOptions redirectOptsFalse = HttpOptions.empty()
        .followRedirects(false);

    HttpOptions redirectOptsTrue = HttpOptions.empty();

    HttpOptions mergeTrueOnTopOfFalse = redirectOptsTrue.mergeOnTopOf(redirectOptsFalse);

    assertThat(mergeTrueOnTopOfFalse.getFollowRedirects(), equalTo(false));


  }

  @Test
  public void mergeFalseOnTrueShouldStayFalse() {

    HttpOptions redirectOptsFalse = HttpOptions.empty()
        .followRedirects(false);

    HttpOptions redirectOptsTrue = HttpOptions.empty();

    HttpOptions mergeTrueOnTopOfFalse = redirectOptsFalse.mergeOnTopOf(redirectOptsTrue);

    assertThat(mergeTrueOnTopOfFalse.getFollowRedirects(), equalTo(false));
  }

  @Test
  public void mergeTrueOnTrueShouldBeTrue() {

    HttpOptions redirectOptsTrue = HttpOptions.empty();

    HttpOptions redirectOptsTrue1 = HttpOptions.empty();

    HttpOptions mergeTrueOnTrue = redirectOptsTrue.mergeOnTopOf(redirectOptsTrue1);

    assertThat(mergeTrueOnTrue.getFollowRedirects(), equalTo(true));
  }

  @Test
  public void mergeFalseOnFalseShouldBeFalse() {

    HttpOptions redirectOptsFalse = HttpOptions.empty().followRedirects(false);

    HttpOptions redirectOptsFalse1 = HttpOptions.empty().followRedirects(false);

    HttpOptions mergeFalseOnFalse = redirectOptsFalse.mergeOnTopOf(redirectOptsFalse1);

    assertThat(mergeFalseOnFalse.getFollowRedirects(), equalTo(false));
  }


}
