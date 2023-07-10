package com.teststeps.thekla4j.http.httpConn;

import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;

public class HcHttpResult implements HttpResult {

    private String response = "";
    private Integer statusCode = 0;
    private HashMap<String, List<String>> headers = HashMap.empty();


    public static HcHttpResult response(String response) {
        return new HcHttpResult(response);
    }

    public HcHttpResult statusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HcHttpResult headers(HashMap<String, List<String>> headers) {
        this.headers = headers;
        return this;
    }

    private HcHttpResult(String response) {
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

    public HashMap<String, List<String>> headers() {
        return this.headers;
    }

    public String toString() {
        return this.toString(0);
    }

    public String toString(int indent) {
        return JSON.logOf(this).replace("\n","\t".repeat(indent) + "\n");
//        return LogUtils.indentStr.apply(indent) +
//            "RestResult: " + LogUtils.indentStrNL.apply(indent) + LogUtils.standardIndent +
//            "Status: " + this.statusCode + LogUtils.indentStrNL.apply(indent) + LogUtils.standardIndent +
//            "Response: " + this.response  + LogUtils.indentStrNL.apply(indent) + LogUtils.standardIndent +
//            "Headers: " + this.headers;
    }
}
