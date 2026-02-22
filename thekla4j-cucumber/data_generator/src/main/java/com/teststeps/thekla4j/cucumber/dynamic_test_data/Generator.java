package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field or method as a data generator.
 *
 * <p><b>On fields:</b> The annotated field must be of type {@link DataGenerator}.</p>
 * <pre>
 * {@code
 * @Generator(name = "myGenerator", description = "Generates test data")
 * public final DataGenerator myGenerator = (data) -> Try.of(() -> "result");
 * }
 * </pre>
 *
 * <p><b>On methods:</b> The annotated method must return a {@link DataGenerator}.
 * The method parameters will be populated from the generator parameter string.
 * Parameter names in the generator call must match the method parameter names.</p>
 * <pre>
 * {@code
 * @Generator(name = "myGenerator")
 * public DataGenerator myGenerator(String one, String two) {
 *     return parameterMap -> Try.of(() -> one + " " + two);
 * }
 * }
 * </pre>
 * <p>When called as {@code myGenerator{one: hello, two: world}}, the method parameters
 * {@code one} and {@code two} will be set to "hello" and "world" respectively.</p>
 *
 * <p>If the {@code name} attribute is omitted, the field or method name will be used as the generator name.</p>
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Generator {

  /**
   * The name of the generator. If empty, the field name will be used.
   *
   * @return the generator name
   */
  String name() default "";

  /**
   * A description of what this generator does.
   *
   * @return the generator description
   */
  String description() default "no description given";
}

