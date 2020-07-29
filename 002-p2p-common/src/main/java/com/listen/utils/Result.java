package com.listen.utils;

import java.util.HashMap;

/**
 * @Author: Listen
 * @Date: 2020/7/27
 */
public class Result extends HashMap<String,Object> {

    public static Result success(){
        Result result = new Result();
        result.put("code", 1);
        result.put("success", true);
        return result;
    }

    public static Result error(String message){
        Result result = new Result();
        result.put("code", -1);
        result.put("message", message);
        result.put("success", false);
        return result;
    }

    public static Result success(String randomCode) {
        Result result = new Result();
        result.put("code", 1);
        result.put("data", randomCode);
        result.put("success", true);
        return result;
    }
}
