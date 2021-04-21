package io.github.thanhduybk.common.payload.request;

import io.github.thanhduybk.common.payload.constant.Constant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Set;

public abstract class PageableRequest {
    @Min(1)
    protected Integer page;

    @Min(1)
    @Max(100)
    protected Integer size;

    protected String sort;

    protected String order;

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

    public String getSort() {
        return sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    private Sort getSortObject() {
        if (!StringUtils.hasText(sort) || !getSortableFields().contains(sort)) {
            return Sort.unsorted();
        }

        return isValidOrder() ? Sort.by(order, sort) : Sort.by(Sort.Direction.ASC, sort);
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Pageable getPageable() {
        return PageRequest.of(getPage() - 1, getSize(), getSortObject());
    }

    protected boolean isValidOrder() {
        return Sort.Direction.fromOptionalString(order).isPresent();
    }

    protected abstract Set<String> getSortableFields();
}
