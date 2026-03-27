package com.teststeps.thekla4j.allure.cucumber.plugins.testsourcemodel;

import io.cucumber.messages.types.Feature;
import io.cucumber.messages.types.Scenario;
import io.cucumber.plugin.event.TestSourceRead;
import java.net.URI;

/**
 * Proxy for {@link TestSourcesModel} that exposes methods to register and query
 * Cucumber test source information.
 */
public class TestSourcesModelProxy {

  private final TestSourcesModel testSources;

  /**
   * Creates a new {@code TestSourcesModelProxy} with a fresh internal {@link TestSourcesModel}.
   */
  public TestSourcesModelProxy() {
    this.testSources = new TestSourcesModel();
  }

  /**
   * Registers a {@link TestSourceRead} event for the given URI.
   *
   * @param path  the URI of the feature file
   * @param event the test source read event
   */
  public void addTestSourceReadEvent(final URI path, final TestSourceRead event) {
    testSources.addTestSourceReadEvent(path, event);
  }

  /**
   * Returns the Cucumber {@link Feature} associated with the given URI.
   *
   * @param path the URI of the feature file
   * @return the feature model, or {@code null} if not found
   */
  public Feature getFeature(final URI path) {
    return testSources.getFeature(path);
  }

  /**
   * Returns the {@link Scenario} definition for the given URI and line number.
   *
   * @param path the URI of the feature file
   * @param line the line number of the scenario
   * @return the scenario definition
   */
  public Scenario getScenarioDefinition(final URI path, final int line) {
    return TestSourcesModel.getScenarioDefinition(testSources.getAstNode(path, line));
  }
}
