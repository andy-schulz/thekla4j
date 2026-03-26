package com.teststeps.thekla4j.allure.cucumber.plugins.testsourcemodel;

import io.cucumber.messages.types.Feature;
import io.cucumber.messages.types.Scenario;
import io.cucumber.plugin.event.TestSourceRead;
import java.net.URI;

public class TestSourcesModelProxy {

  private final TestSourcesModel testSources;

  public TestSourcesModelProxy() {
    this.testSources = new TestSourcesModel();
  }

  public void addTestSourceReadEvent(final URI path, final TestSourceRead event) {
    testSources.addTestSourceReadEvent(path, event);
  }

  public Feature getFeature(final URI path) {
    return testSources.getFeature(path);
  }

  public Scenario getScenarioDefinition(final URI path, final int line) {
    return TestSourcesModel.getScenarioDefinition(testSources.getAstNode(path, line));
  }
}
