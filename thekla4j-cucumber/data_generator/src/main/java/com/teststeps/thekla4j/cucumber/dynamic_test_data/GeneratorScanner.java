package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Scans objects for fields and methods annotated with {@link Generator} and registers them in a {@link GeneratorStore}.
 */
@Log4j2
public class GeneratorScanner {

  /**
   * Scan the given provider objects for fields and methods annotated with {@link Generator}
   * and register them in the given {@link GeneratorStore}.
   *
   * @param store     the generator store to register generators in
   * @param providers the objects to scan for annotated generator fields and methods
   * @return the generator store with all discovered generators registered
   */
  public static GeneratorStore scan(GeneratorStore store, Object... providers) {

    for (Object provider : providers) {
      List.of(provider.getClass().getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Generator.class))
        .forEach(field -> registerField(store, provider, field));

      List.of(provider.getClass().getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(InlineGen.class))
        .forEach(field -> registerInlineField(store, provider, field));

      List.of(provider.getClass().getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(Generator.class))
        .forEach(method -> registerMethod(store, provider, method));
    }

    return store;
  }

  private static void registerField(GeneratorStore store, Object provider, Field field) {

    if (!DataGenerator.class.isAssignableFrom(field.getType())) {
      throw new IllegalArgumentException(
        "Field '" + field.getName() + "' annotated with @Generator must be of type DataGenerator, but is " + field.getType().getSimpleName());
    }

    Generator annotation = field.getAnnotation(Generator.class);
    String name = annotation.name().isEmpty() ? field.getName() : annotation.name();
    String description = annotation.description();

    try {
      field.setAccessible(true);
      DataGenerator generator = (DataGenerator) field.get(provider);

      if (generator == null) {
        throw new IllegalArgumentException("Field '" + field.getName() + "' annotated with @Generator is null");
      }

      store.addGeneratorInternal(name, description, generator);
      log.debug("Registered generator '{}' from field '{}'", name, field.getName());
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Cannot access field '" + field.getName() + "' annotated with @Generator", e);
    }
  }

  private static void registerInlineField(GeneratorStore store, Object provider, Field field) {

    if (!InlineGenerator.class.isAssignableFrom(field.getType())) {
      throw new IllegalArgumentException(
        "Field '" + field.getName() + "' annotated with @InlineGen must be of type InlineGenerator, but is " + field.getType().getSimpleName());
    }

    InlineGen annotation = field.getAnnotation(InlineGen.class);
    String name = annotation.name().isEmpty() ? field.getName() : annotation.name();

    try {
      field.setAccessible(true);
      InlineGenerator generator = (InlineGenerator) field.get(provider);

      if (generator == null) {
        throw new IllegalArgumentException("Field '" + field.getName() + "' annotated with @InlineGen is null");
      }

      store.addInlineGenerator(name, generator);
      log.debug("Registered inline generator '{}' from field '{}'", name, field.getName());
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Cannot access field '" + field.getName() + "' annotated with @InlineGen", e);
    }
  }

  private static void registerMethod(GeneratorStore store, Object provider, Method method) {

    if (!DataGenerator.class.isAssignableFrom(method.getReturnType())) {
      throw new IllegalArgumentException(
        "Method '" + method.getName() + "' annotated with @Generator must return DataGenerator, but returns " + method.getReturnType().getSimpleName());
    }

    Generator annotation = method.getAnnotation(Generator.class);
    String name = annotation.name().isEmpty() ? method.getName() : annotation.name();
    String description = annotation.description();

    Parameter[] methodParameters = method.getParameters();
    List<String> parameterNames = List.of(methodParameters).map(Parameter::getName);

    method.setAccessible(true);

    // Create a wrapper DataGenerator that:
    // 1. Extracts method parameter values from the generator parameter map
    // 2. Invokes the annotated method with those values
    // 3. Delegates to the returned DataGenerator
    DataGenerator wrapper = parameterMap -> {
      try {
        Object[] args = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
          String paramName = parameterNames.get(i);
          Class<?> paramType = methodParameters[i].getType();

          String value = parameterMap.get(paramName)
            .getOrElseThrow(() -> new IllegalArgumentException(
              "Missing parameter '" + paramName + "' for generator method '" + method.getName() + "'. " +
                "Expected parameters: " + parameterNames.mkString(", ")));

          args[i] = convertParameter(value, paramType, paramName, method.getName());
        }

        DataGenerator innerGenerator = (DataGenerator) method.invoke(provider, args);

        if (innerGenerator == null) {
          return Try.failure(new IllegalStateException(
            "Generator method '" + method.getName() + "' returned null"));
        }

        // pass remaining parameters (those not consumed by method params) to the inner generator
        Map<String, String> remainingParams = parameterNames
          .foldLeft(parameterMap, (map, pName) -> map.remove(pName));

        return innerGenerator.run(remainingParams);
      } catch (IllegalArgumentException e) {
        return Try.failure(e);
      } catch (Exception e) {
        return Try.failure(new RuntimeException(
          "Failed to invoke generator method '" + method.getName() + "'", e));
      }
    };

    store.addGeneratorInternal(name, description, wrapper);
    log.debug("Registered generator '{}' from method '{}'", name, method.getName());
  }

  private static Object convertParameter(String value, Class<?> targetType, String paramName, String methodName) {
    if (targetType == String.class) {
      return value;
    } else if (targetType == int.class || targetType == Integer.class) {
      try {
        return Integer.parseInt(value);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(
          "Parameter '" + paramName + "' of generator method '" + methodName + "' expects an integer, but got: " + value);
      }
    } else if (targetType == long.class || targetType == Long.class) {
      try {
        return Long.parseLong(value);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(
          "Parameter '" + paramName + "' of generator method '" + methodName + "' expects a long, but got: " + value);
      }
    } else if (targetType == boolean.class || targetType == Boolean.class) {
      return Boolean.parseBoolean(value);
    } else if (targetType == double.class || targetType == Double.class) {
      try {
        return Double.parseDouble(value);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(
          "Parameter '" + paramName + "' of generator method '" + methodName + "' expects a double, but got: " + value);
      }
    } else {
      throw new IllegalArgumentException(
        "Unsupported parameter type '" + targetType.getSimpleName() + "' for parameter '" + paramName +
          "' of generator method '" + methodName + "'. Supported types: String, int, long, boolean, double");
    }
  }

  private GeneratorScanner() {
    // utility class
  }
}

