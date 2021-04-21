package io.github.thanhduybk.common.payload.request;

import io.github.thanhduybk.common.payload.constant.Constant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class PageableRequest {
    @Min(1)
    protected Integer page;

    @Min(1)
    @Max(100)
    protected Integer size;

    protected List<String> sorts = new ArrayList<>();

    protected List<String> orders = new ArrayList<>();

    public int getPage() {
        return page != null ? page : Constant.DEFAULT_PAGE;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getSize() {
        return size != null ? size : Constant.DEFAULT_SIZE;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    protected List<String> getSorts() {
        return sorts;
    }

    public void setSorts(List<String> sorts) {
        this.sorts = sorts;
    }

    protected List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public Pageable getPageable() {
        return PageRequest.of(getPage() - 1, getSize(), getSortObject());
    }

    protected Sort getSortObject() {
        if (sorts == null || sorts.isEmpty()) {
            return Sort.unsorted();
        }

        int orderSize = orders == null ? 0 : orders.size();

        List<Sort.Order> sortOrders = new ArrayList<>();

        for (int i = 0, sortsSize = sorts.size(); i < sortsSize; i++) {
            String sort = sorts.get(i);

            if (i < orderSize && isValidOrder(orders.get(i)) && getSortableFields().contains(sort)) {
                sortOrders.add(new Sort.Order(Sort.Direction.fromString(orders.get(i)), sort));
            }

            if (i >= orderSize) break;
        }

        return Sort.by(sortOrders);
    }

    protected boolean isValidOrder(String order) {
        return Sort.Direction.fromOptionalString(order).isPresent();
    }

    protected abstract Set<String> getSortableFields();
}
