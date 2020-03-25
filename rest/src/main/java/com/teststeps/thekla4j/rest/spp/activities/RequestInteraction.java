package com.teststeps.thekla4j.rest.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.rest.core.RestRequest;
import com.teststeps.thekla4j.rest.core.RestResult;
import com.teststeps.thekla4j.rest.spp.Request;
import com.teststeps.thekla4j.rest.spp.RestOptions;
import com.teststeps.thekla4j.rest.spp.abilities.UseTheRestApi;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.function.Function;

public class RequestInteraction<ReqT extends Interaction<ReqT, Void, RestResult>> extends Interaction<ReqT, Void, RestResult> {

    protected Request request;
    protected RestOptions restOptions = RestOptions.empty();
    private Function<RestRequest,Either<Throwable, RestResult>> requestMethod;

    @Called("resource") // is set when request is assigned to Post interaction
    public String logResource = "";

    @Called("options") // is set when RestOptions are assigned to Post interaction
    public RestOptions logOptions = RestOptions.empty();

    @Override
    public Either<Throwable, RestResult> performAs(Actor actor, Void result) {
        final Try<Either<Throwable, RestResult>> t =
                UseTheRestApi.as(actor)
                        .map(useRestAbility -> useRestAbility.send(this.request, this.restOptions)) // returns Either
                        .map(eReq -> eReq.flatMap(this.requestMethod)); // returns Either

        return t.isSuccess() ? t.get() : Either.left(t.getCause());
    }

    public ReqT options(RestOptions opts) {
        this.restOptions = opts;
        // logging purpose
        this.logOptions = opts.mergeOnTopOf(this.logOptions);

        return (ReqT)this;
    }

    public RequestInteraction(Request request, Function<RestRequest,Either<Throwable, RestResult>> requestMethod) {
        this.request = request;
        this.requestMethod = requestMethod;

        // for logging purposes
        this.logResource = request.resource();
        this.logOptions = request.options().mergeOnTopOf(this.logOptions);
    }
}