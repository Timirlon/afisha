package com.practice.mainsvc.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@UtilityClass
public class PageRequestConstants {
    public static Pageable getDefault(int from, int pageSize) {
        int pageNumber = from / pageSize;

        return PageRequest.of(pageNumber, pageSize);
    }
}
