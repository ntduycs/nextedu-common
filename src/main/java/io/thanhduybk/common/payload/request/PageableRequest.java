package io.thanhduybk.common.payload.request;

import io.thanhduybk.common.payload.constant.Constant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class PageableRequest {
    @Min(1)
    protected Integer page;

    @Min(1)
    @Max(100)
    protected Integer size;

    @Pattern(regexp = "^[A-Za-z]+,(asc|desc)$", message = "must in form of \"prop,(asc|desc)\", example: \"updatedAt,desc\"")
    protected String sort;

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

    private Sort getSortObject() {
        if (!StringUtils.hasText(sort)) {
            return Sort.unsorted();
        }

        String[] sortCriteria = sort.split(",", 2);

        return sortCriteria.length < 2
                ? Sort.unsorted()
                : Sort.by(Sort.Direction.valueOf(sortCriteria[1].toUpperCase()), sortCriteria[0]);
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Pageable getPageable() {
        return PageRequest.of(getPage() - 1, getSize(), getSortObject());
    }
}
