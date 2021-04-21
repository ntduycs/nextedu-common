package io.github.thanhduybk.common.payload.constant;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final int FIRST_PAGE = 1;
    public static final int DEFAULT_PAGE = FIRST_PAGE;
    public static final int DEFAULT_SIZE = 10;
    public static final String DEFAULT_SORT_COLUMN = "updatedAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ssXXX");
}
