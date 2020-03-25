package com.teststeps.thekla4j.rest.spp.abilities;

import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.UsesAbilities;
import com.teststeps.thekla4j.rest.core.RestClient;
import com.teststeps.thekla4j.rest.core.RestRequest;
import com.teststeps.thekla4j.rest.spp.Request;
import com.teststeps.thekla4j.rest.spp.RestOptions;
import io.vavr.control.Either;
import io.vavr.control.Try;

public class UseTheRestApi implements Ability {

    private RestClient restClient;

    public static Try<UseTheRestApi> as(UsesAbilities actor)  {
        return Try.of(() -> (UseTheRestApi)actor.withAbilityTo(UseTheRestApi.class));
    }

    public static UseTheRestApi with(RestClient restClient) {
        return new UseTheRestApi(restClient);
    }

    public Either<Throwable, RestRequest> send(Request spe, RestOptions activityOptions) {
        return spe.send(this.restClient, activityOptions);
    }

    private UseTheRestApi(RestClient restClient) {
        this.restClient = restClient;
    }
}