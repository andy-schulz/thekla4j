package com.teststeps.thekla4j.examples.cucumber;

import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.Suite;

/**
 * Cucumber test runner for the activity log examples.
 *
 * <p>Uses the Thekla4jAllureCucumberPlugin for Allure reporting and includes the
 * allure cucumber hook package in the glue to enable automatic activity log mapping.
 */
@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(
    key = FEATURES_PROPERTY_NAME, value = "src/test/resources/features")
@ConfigurationParameter(
    key = GLUE_PROPERTY_NAME, value = "com.teststeps.thekla4j.examples.cucumber.stepdefs," + "com.teststeps.thekla4j.allure.cucumber.plugins")
@ConfigurationParameter(
    key = PLUGIN_PROPERTY_NAME, value = "com.teststeps.thekla4j.allure.cucumber.plugins.Thekla4jAllureCucumberPlugin")
class CucumberExampleTest {
}
