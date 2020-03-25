package com.teststeps.thekla4j.rest.spp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teststeps.thekla4j.core.util.LanguageCandy;

import java.lang.reflect.Type;
import java.util.HashMap;

public class RestOptions extends LanguageCandy<RestOptions> {

    public HashMap<String, String> headers = new HashMap<String, String>();
    public HashMap<String, String> parameters = new HashMap<String, String>();
    public String baseUrl = "";
    public String body = "";


    public RestOptions header(String headerName, String headerValue) {
        return getNewRestOptions()
                .setHeaderValue(headerName, headerValue);
    }

    public RestOptions baseUrl(String baseUrl) {
        return getNewRestOptions()
                .setBaseUrl(baseUrl);
    }

    public RestOptions body(String body) {
        return getNewRestOptions()
                .setBody(body);
    }

    public RestOptions parameter(String parameterName, String parameterValue) {
        return getNewRestOptions()
                .setParameterValue(parameterName, parameterValue);
    }

    public RestOptions clone() {
        return getNewRestOptions();
    }

    public RestOptions mergeOnTopOf(RestOptions mergedOpts) {
        if (this.baseUrl.length() > 0)
            mergedOpts.setBaseUrl(this.baseUrl);

        if (this.body.length() > 0)
            mergedOpts.setBody(this.body);

        this.headers.forEach(mergedOpts::setHeaderValue);

        this.parameters.forEach(mergedOpts::setParameterValue);

        return mergedOpts;
    }

    /**
     * Privates
     */

    private RestOptions getNewRestOptions() {
        return new RestOptions(this.headers, this.parameters, this.baseUrl, this.body);
    }

    private RestOptions setBody(String body) {
        this.body = body;
        return this;
    }

    private RestOptions setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    private RestOptions setParameterValue(String parameterName, String parameterValue) {
        return this.setMapValues(this.parameters, parameterName, parameterValue);
    }

    private RestOptions setHeaderValue(String headerName, String headerValue) {
        return this.setMapValues(this.headers, headerName, headerValue);
    }

    private RestOptions setMapValues(HashMap map, String mapItemName, String mapItemValue) {

        if (map.containsKey(mapItemName)) {
            map.replace(mapItemName, mapItemValue);
        } else {
            map.put(mapItemName, mapItemValue);
        }
        return this;
    }

    /**
     * constructors
     */


    private RestOptions(HashMap<String, String> headers,
                        HashMap<String, String> parameters,
                        String baseUrl,
                        String body) {
        // clone fields of request

        this.baseUrl = baseUrl;
        this.body = body;

        Gson gson = new Gson();

        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();

        String headersJson = gson.toJson(headers);
        this.headers = gson.fromJson(headersJson, type);


        String parametersJson = gson.toJson(parameters);
        this.parameters = gson.fromJson(parametersJson, type);
    }

    public String toString() {
        return
                "\nBaseUrl: " + this.baseUrl +
                        "\nParameters: " +
                        this.parameters.toString() +
                        "\nHeaders: " +
                        this.headers.toString() +
                        "\nBody: " + this.body +
                        "\n";
    }

    private RestOptions() {

    }

    public static RestOptions empty() {
        return new RestOptions();
    }
}
