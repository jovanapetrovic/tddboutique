package com.jovana.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by jovana on 28.03.2020
 */
public class ObjectToJsonMapper {

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
