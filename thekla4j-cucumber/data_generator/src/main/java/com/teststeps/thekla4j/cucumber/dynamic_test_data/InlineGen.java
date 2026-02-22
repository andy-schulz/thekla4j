package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as an inline data generator.
 *
 * <p>The annotated field must be of type {@link InlineGenerator}.</p>
 * <pre>
 * {@code
 * @InlineGen(name = "UUID")
 * public final InlineGenerator uuidGen = () -> Try.of(() -> UUID.randomUUID().toString());
 * }
 * </pre>
 *
 * <p>If the {@code name} attribute is omitted, the field name will be used as the generator name.</p>
 *
 * <p>Register providers via {@link GeneratorStore#registerGenerators(Object...)}:</p>
 * <pre>
 * {@code
 * GeneratorStore store = GeneratorStore.create()
 *     .registerGenerators(new MyInlineGeneratorProvider());
 * }
 * </pre>
 *
 * <p>Use inline generators in strings with the {@code ?{NAME}} syntax:</p>
 * <pre>
 * {@code
 * String result = store.parseAndExecute("order-?{UUID}");
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InlineGen {

  /**
   * The name of the inline generator. If empty, the field name will be used.
   *
   * @return the inline generator name
   */
  String name() default "";

  /**
   * A description of what this inline generator does.
   *
   * @return the inline generator description
   */
  String description() default "no description given";
}

