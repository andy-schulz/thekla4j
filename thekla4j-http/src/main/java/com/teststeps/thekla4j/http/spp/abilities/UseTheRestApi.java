package com.teststeps.thekla4j.http.spp.abilities;

import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.UsesAbilities;
import com.teststeps.thekla4j.http.core.HttpClient;
import com.teststeps.thekla4j.http.core.HttpRequest;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;

public class UseTheRestApi implements Ability {

    private HttpClient httpClient;

    public static Try<UseTheRestApi> as(UsesAbilities actor)  {
        return Try.of(() -> (UseTheRestApi)actor.withAbilityTo(UseTheRestApi.class));
    }

    public static UseTheRestApi with(HttpClient httpClient) {
        return new UseTheRestApi(httpClient);
    }

    public Either<Throwable, HttpRequest> send(Request spe, HttpOptions activityOptions) {
        return this.httpClient.request(spe, activityOptions);
    }

    private UseTheRestApi(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void destroy() {
        httpClient.destroy();
    }

    @Override
    public List<NodeAttachment> abilityLogDump() {
        return List.empty();
    }
}