package io.github.thanhduybk.common.payload.response.builder;

import io.github.thanhduybk.common.payload.response.ListResponse;

import java.util.Collections;
import java.util.List;

public class ListResponseBuilder {
    public static <T> ListResponse<T> build(List<T> items) {
        ListResponse<T> response = new ListResponse<>();

        response.setItems(items != null ? items : Collections.emptyList());
        response.setTotalElements(items != null ? items.size() : 0);

        return response;
    }
}
