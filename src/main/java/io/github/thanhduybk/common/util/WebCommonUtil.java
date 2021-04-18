package io.github.thanhduybk.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class WebCommonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static Map<String, Object> object2Map(Object object) {
        return objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }

    public static <T> T map2Object(Map<String, Object> map, Class<T> oClass) {
        return objectMapper.convertValue(map, oClass);
    }
}
