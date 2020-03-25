package com.teststeps.thekla4j.rest.httpConn;

import com.teststeps.thekla4j.rest.core.RestRequest;
import com.teststeps.thekla4j.rest.core.RestResult;
import com.teststeps.thekla4j.rest.spp.RestOptions;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

public class HCRestRequest implements RestRequest {

    public static class HCRequestHelper {
        private String resource = "";

        public RestRequest using(RestOptions opts) throws Exception {
            return new HCRestRequest(this.resource, opts);
        }

        HCRequestHelper(String resource) {
            this.resource = resource;
        }
    }

    private HttpURLConnection connection;

    private RestOptions opts;
    private String resource = "";

    public static HCRequestHelper on(String resource) {
        return new HCRequestHelper(resource);
    }

    private Try<HttpURLConnection> executeRequest(String method) {
        return
                Try.of(() -> {
                    this.connection.setRequestMethod(method);
                    return this.connection;
                }).map(con -> {
                    con.setDoOutput(true);
                    return con;
                });
    }

    @Override
    public Either<Throwable, RestResult> get() {
        Try<HttpURLConnection> t = this.executeRequest("GET");

        return t.isSuccess() ? t.map(this::send).get() : Either.left(t.getCause());
    }

    @Override
    public Either<Throwable, RestResult> post() {

        Try<HttpURLConnection> t = this.executeRequest("POST")
                .mapTry(con -> {
                    if (this.opts.body != null) {
                        OutputStream os = con.getOutputStream();
                        byte[] input = this.opts.body.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                    return con;
                });

        return t.isSuccess() ? t.map(this::send).get() : Either.left(t.getCause());
    }

    @Override
    public Either<Throwable, RestResult> patch() {
        return null;
    }

    @Override
    public Either<Throwable, RestResult> put() {
        return null;
    }

    @Override
    public Either<Throwable, RestResult> delete() {
        return null;
    }

    private Try<HttpURLConnection> init(String resource, RestOptions opts) {
        return Try.of(() -> new URL(getUrl(opts.baseUrl, resource, opts.parameters)))
                .mapTry(url -> (HttpURLConnection) url.openConnection())
                .map(con -> this.setHeaders(con, opts.headers));
    }

    private HttpURLConnection setHeaders(HttpURLConnection con, HashMap<String, String> headers) {
        headers.forEach(con::setRequestProperty);
        return con;
    }

    private String getUrl(String baseUrl, String resource, HashMap<String, String> parameters) {
        return (baseUrl.length() > 0 ? baseUrl.concat(resource) : resource)
                .concat("?")
                .concat(getParameterString(parameters));
    }

    private String getParameterString(HashMap<String, String> params) {
        return params.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    private Either<Throwable, RestResult> send(HttpURLConnection con) {

        Try<HCRestResult> t = Try.of(() -> {

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return HCRestResult
                    .response(response.toString())
                    .statusCode(con.getResponseCode());

        });

        return t.isSuccess() ? Either.right(t.get()) : Either.left(t.getCause());

    }

    public HCRestRequest(final String resource, final RestOptions opts) throws Exception {
        this.resource = resource;
        this.connection = this.init(resource, opts)
                .getOrElseThrow(ex -> new Exception(ex.toString()));
//                .onSuccess(con -> this.connection = con);
        this.opts = opts;
    }
}
