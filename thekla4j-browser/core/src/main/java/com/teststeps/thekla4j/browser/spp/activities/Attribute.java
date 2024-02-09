package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Action("get content of attribute '@{attributeName}' of @{element}")
public class Attribute extends Task<Void, String> {

  @Called(name = "attributeName")
  private String attributeName;
  @Called(name = "element")
  private Element element;
  @Override
  protected Either<ActivityError, String> performAs(Actor actor, Void result) {
    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.attributeValueOf(attributeName, element))
        .toEither(ActivityError.with(String.format("could not get value of attribute '%s' from %s", attributeName, element)));
  }

  public static AttributeNamed named(String name) {
    return new AttributeNamed(name);
  }

  public static class AttributeNamed {
    private final String name;

    public AttributeNamed(String name) {
      this.name = name;
    }

    protected Attribute of(Element element) {
      return new Attribute(name, element);
    }
  }
}
