package com.example.sync_book.util;

import com.jayway.jsonpath.JsonPath;
import lombok.experimental.UtilityClass;

import static com.example.sync_book.util.JsonUtil.writeValue;

@UtilityClass
public class CommonTestData {
    public static final int NOT_FOUND_ID = 1000;
    public static final String NOT_FOUND_MESSAGE = "Entity with id=" + NOT_FOUND_ID + " not found";
    public static final String USER1_ID = "User_1_id";

    public static <T> T asParsedJson(Object obj) {
        String json = writeValue(obj);
        return JsonPath.read(json, "$");
    }
}