package com.teststeps.thekla4j.websocket.stomp.core;

import com.teststeps.thekla4j.utils.json.JSON;
import lombok.With;

@With
public record Endpoint(
    String name,
    String url,
    StompHeaders headers,
    boolean trackReceipts

    ) {

    public static Endpoint of(String name, String url, StompHeaders headers) {
        return new Endpoint(
            name,
            url,
            headers,
            false);
    }

    public String toString() {
        return JSON.logOf(this);
    }
}
