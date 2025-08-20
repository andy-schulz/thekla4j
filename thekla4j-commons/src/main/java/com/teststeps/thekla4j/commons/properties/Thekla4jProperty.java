package com.teststeps.thekla4j.commons.properties;

import static io.vavr.API.*;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "Thekla4jProperty")
public class Thekla4jProperty {

  private final static String PROPERTY_FILE = "thekla4j.properties";

  private static Function1<PropertyElement, Option<String>> of = TestPropertyHelper._withName.memoized();
  private static Function1<PropertyElement, Try<Integer>> asInteger = TestPropertyHelper._asInteger.memoized();

  /**
   * get the value of the property
   * <p>
   * param: property element returns: property value
   */
  public static Option<String> of(PropertyElement element) {
    return Thekla4jProperty.of.apply(element);
  }

  public static Try<Integer> asInteger(PropertyElement element) {
    return Thekla4jProperty.asInteger.apply(element);
  }

  public static void resetPropertyCache() {
    Thekla4jProperty.of = TestPropertyHelper._withName.memoized();
    Thekla4jProperty.asInteger = TestPropertyHelper._asInteger.memoized();
    TestPropertyHelper.resetPropertyFileCache();
  }

  private Thekla4jProperty() {
    // prevent instantiation of utility class
  }


  static final class TestPropertyHelper {

    private TestPropertyHelper() {
      // prevent instantiation of utility class
    }

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

    private static Function1<String, Try<Properties>> loadPropertyFile = TestPropertyHelper._loadProperty.memoized();


    /**
     * reset the property file cache
     */
    public static void resetPropertyFileCache() {
      TestPropertyHelper.loadPropertyFile = TestPropertyHelper._loadProperty.memoized();
    }

    /**
     * get property from property object
     */
    private static final Function2<String, Properties, Try<String>> getProperty =
        (propertyName, props) -> Option.of(props.getProperty(propertyName))
            .toTry(() -> new Exception("No property with name '" + propertyName + "' found"));

    /**
     * check if the property is empty
     */
    private static final Function2<String, String, Try<String>> checkEmptyValue =
        (name, value) -> Match(value).of(
          Case($(""::equals), () -> Try.failure(new Exception("Property '" + name + "' is empty"))),
          Case($(), () -> Try.success(value)));

    /**
     * load the property from System
     */
    private static final Function1<String, Try<String>> loadSystemProperty =
        propertyName -> Option.of(System.getProperty(propertyName))
            .toTry(() -> new Exception("System Property '" + propertyName + "' does not exist"))
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

    /**
     * get the property value
     */
    static final Function1<PropertyElement, Option<String>> _withName = property -> {
      Try<String> system = TestPropertyHelper.loadSystemProperty.apply(property.name());

      if (system.isSuccess())
        return Option.of(system.get());

      system.onFailure(x -> log.debug("{} - trying to load from property file", x.getMessage()));

      Try<String> envProperty = TestPropertyHelper.loadFileProperty.apply(property.name());

      if (envProperty.isSuccess())
        return Option.of(envProperty.get());

      envProperty.onFailure(x -> log.debug("{} - loading default value now", x.getMessage()));

      return property.defaultValue();
    };

    /**
     * get the as Integer property value
     */
    static final Function1<PropertyElement, Try<Integer>> _asInteger =
        property -> _withName.apply(property)
            .transform(o -> o.isEmpty() ? Try.<String>failure(new Exception("Property not found: " + property.name() +
                ". Its a framework problem.")) : Try.success(o.get()))
            .mapTry(Integer::parseInt);
  }
}
