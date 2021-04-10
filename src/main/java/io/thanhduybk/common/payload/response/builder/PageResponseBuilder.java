package io.thanhduybk.common.payload.response.builder;

import io.thanhduybk.common.payload.constant.Constant;
import io.thanhduybk.common.payload.response.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponseBuilder {
    /**
     * @param items         returned data items
     * @param page          requested page
     * @param size          requested page size
     * @param totalElements total data elements
     * @param <T>           returned data type
     * @return paginated data items payload
     */
    public static <T> PageResponse<T> build(List<T> items, int page, int size, long totalElements) {
        PageResponse<T> response = new PageResponse<>();

        int totalPages = calculateTotalPages(totalElements, size);

        response.setPage(page);
        response.setSize(size);
        response.setItems(items);
        response.setTotalElements(totalElements);
        response.setTotalPages(totalPages);
        response.setFirst(isFirstPage(page));
        response.setLast(isLastPage(totalPages, page));

        return response;
    }

    public static <T> PageResponse<T> build(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();

        response.setPage(page.getNumber() + 1);
        response.setSize(page.getSize());
        response.setItems(page.getContent());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());

        return response;
    }

    private static int calculateTotalPages(long totalElements, int size) {
        return (int) Math.ceil((double) totalElements / size);
    }

    private static boolean isLastPage(int totalPages, int page) {
        return totalPages == page;
    }

    private static boolean isFirstPage(int page) {
        return page == Constant.FIRST_PAGE;
    }
}
