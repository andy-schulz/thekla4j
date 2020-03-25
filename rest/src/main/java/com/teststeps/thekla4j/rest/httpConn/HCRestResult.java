package com.teststeps.thekla4j.rest.httpConn;

import com.google.gson.Gson;
import com.teststeps.thekla4j.core.util.LanguageCandy;
import com.teststeps.thekla4j.rest.core.RestResult;

public class HCRestResult extends LanguageCandy<HCRestResult> implements RestResult {

    private String response = "";
    private Integer statusCode = 0;


    public static HCRestResult response(String response) {
        return new HCRestResult(response);
    }

    public HCRestResult statusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    private HCRestResult(String response) {
        this.response = response;
    }

    @Override
    public Integer statusCode() {
        return this.statusCode;
    }

    @Override
    public String response() {
        return this.response;
    }

    public String toString() {
        return "\nStatus: " + this.statusCode +
                "\nResponse: " + this.response;
    }
}
