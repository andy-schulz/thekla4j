package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestRequest {

  @Test
  @DisplayName("Test Request creation with resource")
  void testRequestCreationWithResource() {
    Request request = Request.on("someResource");
    assertEquals("someResource", request.resource, "Resource should match the provided value");
  }

  @Test
  @DisplayName("Test description assignment")
  void testDescriptionAssignment() {
    Request request = Request.on("someResource").called("Test Description");
    assertEquals("Test Description", request.description, "Description should match the provided value");
  }

  @Test
  @DisplayName("Test default HttpOptions")
  void testDefaultHttpOptions() {
    Request request = Request.on("someResource");
    assertNotNull(request.options, "Options should not be null");
  }

  @Test
  @DisplayName("test request creation with options")
  void testRequestCreationWithOptions() {
    Request request = Request.on("someResource").withOptions(HttpOptions.empty().port(8080));
    assertNotNull(request.options, "Options should not be null");
    assertEquals(8080, request.options.port, "Port should match the provided value");
  }
}
