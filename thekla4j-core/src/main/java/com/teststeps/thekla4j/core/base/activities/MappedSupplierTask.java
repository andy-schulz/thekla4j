package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Try;

/**
 * A lightweight wrapper that stores a source {@link SupplierTask} together with
 * a single <em>composed</em> mapping function (as {@code Function1<ORIG, Try<RT>>}).
 * <p>
 * Both {@link SupplierTask#map(Function1)} and {@link SupplierTask#mapTry(Function1)}
 * compose into this single wrapper. {@code map(fn)} appends via {@link Try#map},
 * {@code mapTry(fn)} appends via {@link Try#flatMap} — no separate class needed.
 *
 * @param <ORIG> the result type of the original source task
 * @param <RT>   the result type after all composed mappings have been applied
 */
class MappedSupplierTask<ORIG, RT> extends SupplierTask<RT> {

  private final SupplierTask<ORIG> source;
  private final Function1<ORIG, Try<RT>> composedMapper;

  MappedSupplierTask(SupplierTask<ORIG> source, Function1<ORIG, Try<RT>> mapper) {
    this.source = source;
    this.composedMapper = mapper;
  }

  @Override
  protected Either<ActivityError, RT> performAs(Actor actor) {
    return source.performAs(actor)
        .flatMap(orig -> composedMapper.apply(orig)
            .toEither()
            .mapLeft(ActivityError::of));
  }

  <R2> MappedSupplierTask<ORIG, R2> appendMap(Function1<RT, R2> fn) {
    return new MappedSupplierTask<>(source,
                                    orig -> composedMapper.apply(orig).map(fn));
  }

  <R2> MappedSupplierTask<ORIG, R2> appendMapTry(Function1<RT, Try<R2>> fn) {
    return new MappedSupplierTask<>(source,
                                    orig -> composedMapper.apply(orig).flatMap(fn));
  }

  @Override
  public String toString() {
    return source.toString();
  }
}
