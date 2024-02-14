package com.teststeps.thekla4j.commons.properties;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Properties;

import static io.vavr.API.*;

public class Thekla4jProperty {

  private final static String PROPERTY_FILE = "thekla4j.properties";

  public static final Function1<PropertyElement, String> of = TestPropertyHelper._withName.memoized();

  public static String of(PropertyElement element) {
    return Thekla4jProperty.of.apply(element);
  }

  static final class TestPropertyHelper {

    /**
     * load the property file
     */
    private static final Function1<String, Try<Properties>> _loadProperty =
        propertyFile -> Try.of(() -> Thekla4jProperty.class.getClassLoader().getResourceAsStream(propertyFile))
            .mapTry(stream -> {
              Properties prop = new Properties();
              prop.load(stream);
              return prop;
            });
    private static final Function1<String, Try<Properties>> loadPropertyFile = _loadProperty
        .memoized();


    /**
     * get property from property object
     */
    private static final Function2<String, Properties, Try<String>> getProperty =
        (propertyName, props) ->
        Option.of(props.getProperty(propertyName))
        .toTry(() -> new Exception("No property with name '" + propertyName + "' found"));

    /**
     * check if the property is empty
     */
    private static final Function2<String, String, Try<String>> checkEmptyValue = (name, value) -> Match(value).of(
        Case($(""::equals), () -> Try.failure(
            new Exception("Property " +
                name +
                " is empty"))),
        Case($(), () -> Try.success(value)));

    /**
     * load the property from System
     */
    private static final Function1<String, Try<String>> loadSystemProperty =
        propertyName -> Option.of(System.getProperty(propertyName))
            .toTry(() -> new Exception("Property " + propertyName + " does not exist"))
            .flatMap(checkEmptyValue.apply(propertyName));

    /**
     * load the property from file
     * <p>
     * param: property name returns: property value
     * <p>
     * error: property is empty
     */
    public static final Function1<String, Try<String>> loadFileProperty =
        propertyName -> Try.of(() -> PROPERTY_FILE)
            .flatMap(loadPropertyFile)
            .flatMap(getProperty.apply(propertyName));

    static final Function1<PropertyElement, String> _withName = property -> {
      Try<String> system = TestPropertyHelper.loadSystemProperty.apply(property.name());

      if (system.isSuccess())
        return system.get();

      Try<String> envProperty = TestPropertyHelper.loadFileProperty.apply(property.name());

      if (envProperty.isSuccess())
        return envProperty.get();

      return property.defaultValue();
    };
  }
}


