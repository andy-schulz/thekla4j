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

/**
 * Get the content of an attribute of an element
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("get content of attribute '@{attributeName}' of @{element}")
public class Attribute extends SupplierTask<String> {

  @Called(name = "attributeName")
  private String attributeName;
  @Called(name = "element")
  private Element element;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.attributeValueOf(attributeName, element))
        .toEither(ActivityError.of(String.format("could not get value of attribute '%s' from %s", attributeName, element)));
  }

  /**
   * Create a new Attribute activity
   *
   * @param name - the name of the attribute to get the content from
   * @return - a new Attribute activity
   */
  public static AttributeNamed named(String name) {
    return new AttributeNamed(name);
  }

  /**
   * Create a new named attribute activity
   */
  public static class AttributeNamed {
    private final String name;

    /**
     * Create a new named attribute activity
     *
     * @param name - the name of the attribute to get the content from
     */
    public AttributeNamed(String name) {
      this.name = name;
    }

    /**
     * Create a new Attribute activity
     *
     * @param element - the element to get the attribute content from
     * @return - a new Attribute activity
     */
    public Attribute of(Element element) {
      return new Attribute(name, element);
    }
  }
}
