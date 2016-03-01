package com.cooksys.CookPayroll;

import spark.ResponseTransformer;

import com.google.gson.Gson;

public class JsonTransformer implements ResponseTransformer {
 
    private Gson gson = new Gson();
 
    public String render(Object model) {
        return gson.toJson(model);
    }
}
