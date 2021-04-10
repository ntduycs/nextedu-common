package io.thanhduybk.common.payload.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListResponse<T> implements Serializable {
    private List<T> items = new ArrayList<>();
    private long totalElements;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
