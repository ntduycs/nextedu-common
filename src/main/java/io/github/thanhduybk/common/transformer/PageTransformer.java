package io.github.thanhduybk.common.transformer;

import io.github.thanhduybk.common.payload.constant.Constant;
import io.github.thanhduybk.common.payload.response.PageResponse;
import org.springframework.data.domain.Page;

public class PageTransformer<T> implements Transformer<Page<T>, PageResponse<T>> {
    @Override
    public PageResponse<T> transform(Page<T> from) {
        PageResponse<T> pageResponse = new PageResponse<>();

        pageResponse.setItems(from.getContent());
        pageResponse.setPage(from.getNumber() + 1);
        pageResponse.setSize(from.getSize());
        pageResponse.setTotalElements(from.getTotalElements());
        pageResponse.setTotalPages(from.getTotalPages());
        pageResponse.setFirst(from.getNumber() + 1 == Constant.FIRST_PAGE);
        pageResponse.setLast(from.getNumber() == from.getTotalPages() - 1);

        return pageResponse;
    }
}
