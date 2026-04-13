package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;

/**
 * A lightweight wrapper that stores a source {@link Task} together with
 * a single <em>composed</em> mapping function (as {@code Function1<ORIG, Try<RT>>}).
 * <p>
 * Mirrors {@link MappedSupplierTask} for the {@code Task<PT, RT>} hierarchy.
 * Both {@link Task#map(Function1)} and {@link Task#mapTry(Function1)}
 * compose into this single wrapper.
 *
 * @param <PT>   the input type passed to the task
 * @param <ORIG> the result type of the original source task
 * @param <RT>   the result type after all composed mappings have been applied
 */
class MappedTask<PT, ORIG, RT> extends Task<PT, RT> {

  private final Task<PT, ORIG> source;
  private final Function1<ORIG, Try<RT>> composedMapper;

  MappedTask(Task<PT, ORIG> source, Function1<ORIG, Try<RT>> mapper) {
    this.source = source;
    this.composedMapper = mapper;
  }

  @Override
  protected Either<ActivityError, RT> performAs(Actor actor, PT input) {
    return source.performAs(actor, input)
        .flatMap(orig -> composedMapper.apply(orig)
            .toEither()
            .mapLeft(ActivityError::of));
  }

  <R2> MappedTask<PT, ORIG, R2> appendMap(Function1<RT, R2> fn) {
    return new MappedTask<>(source,
                            orig -> composedMapper.apply(orig).map(fn));
  }

  <R2> MappedTask<PT, ORIG, R2> appendMapTry(Function1<RT, Try<R2>> fn) {
    return new MappedTask<>(source,
                            orig -> composedMapper.apply(orig).flatMap(fn));
  }

  @Override
  public String toString() {
    return source.toString();
  }
}
