package io.github.thanhduybk.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thanhduybk.common.payload.constant.Constant;
import io.github.thanhduybk.common.payload.response.Paginated;
import org.springframework.data.domain.Page;

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

    public static <T> Paginated<T> transform(Page<T> origin) {
        Paginated<T> page = new Paginated<>();

        page.setItems(origin.getContent());
        page.setPage(origin.getNumber() + 1);
        page.setSize(origin.getSize());
        page.setTotalElements(origin.getTotalElements());
        page.setTotalPages(origin.getTotalPages());
        page.setFirst(origin.getNumber() + 1 == Constant.FIRST_PAGE);
        page.setLast(origin.getNumber() == origin.getTotalPages() - 1);

        return page;
    }
}
