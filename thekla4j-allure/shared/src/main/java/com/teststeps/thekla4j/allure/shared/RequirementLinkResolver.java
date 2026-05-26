package com.teststeps.thekla4j.allure.shared;

import io.qameta.allure.model.Link;
import io.qameta.allure.util.PropertiesUtils;
import io.qameta.allure.util.ResultsUtils;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Shared requirement-link resolver for Allure integrations.
 */
public final class RequirementLinkResolver {

  private static final String REQUIREMENT_LINK_TYPE = "requirement";
  private static final String THEKLA_REQUIREMENT_LINK_PATTERN_PROPERTY = "thekla4j.req.link.issue.pattern";

  private RequirementLinkResolver() {
  }

  /**
   * Creates an Allure requirement link for the given requirement ID.
   * The link URL is resolved from the {@code thekla4j.req.link.issue.pattern} property.
   *
   * @param requirementId the requirement identifier
   * @return the Allure link
   */
  public static Link createRequirementLink(final String requirementId) {
    final String id = requirementId.trim();
    final String resolvedUrl = resolveTheklaRequirementLinkUrl(id).orElse(null);
    return ResultsUtils.createLink(id, id, resolvedUrl, REQUIREMENT_LINK_TYPE);
  }

  private static Optional<String> resolveTheklaRequirementLinkUrl(final String requirementId) {
    final Properties allureProperties = PropertiesUtils.loadAllureProperties();
    final String pattern = firstNonBlank(
      System.getProperty(THEKLA_REQUIREMENT_LINK_PATTERN_PROPERTY),
      allureProperties.getProperty(THEKLA_REQUIREMENT_LINK_PATTERN_PROPERTY));
    if (pattern.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(pattern.replace("{}", requirementId));
  }

  private static String firstNonBlank(final String... values) {
    return Stream.of(values)
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(value -> !value.isEmpty())
        .findFirst()
        .orElse("");
  }
}
