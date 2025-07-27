package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Get the content of a property of an element
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("get content of property '@{propertyName}' of @{element}")
@Log4j2(topic = "Property")
public class Property extends SupplierTask<String> {

  @Called(name = "propertyName")
  private String propertyName;
  @Called(name = "element")
  private Element element;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .onSuccess(__ -> log.info("Getting property '{}' of element '{}'", propertyName, element))
        .flatMap(b -> b.propertyValueOf(propertyName, element))
        .toEither(ActivityError.of(String.format("could not get value of property '%s' from %s", propertyName, element)));
  }

  /**
   * Create a new Property activity
   *
   * @param name - the name of the property to get the content from
   * @return - a new Property activity
   */
  public static PropertyNamed named(String name) {
    return new PropertyNamed(name);
  }

  /**
   * Create a new named property activity
   */
  public static class PropertyNamed {
    private final String name;

    /**
     * Create a new named property activity
     *
     * @param name - the name of the property to get the content from
     */
    public PropertyNamed(String name) {
      this.name = name;
    }

    /**
     * Create a new Property activity
     *
     * @param element - the element to get the property content from
     * @return - a new Property activity
     */
    public Property of(Element element) {
      return new Property(name, element);
    }
  }
}
