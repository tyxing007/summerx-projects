package io.summerx.framework.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiayg on 7/13/2016.
 */
public class JacksonJsonMapper {

    public static void main(String[] args) throws IOException {

        String carJson =
                "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

        ObjectMapper objectMapper = new ObjectMapper();
        Map m = objectMapper.readValue(carJson, HashMap.class);
        System.out.println(m);

        JsonParser parser;

    }
}
