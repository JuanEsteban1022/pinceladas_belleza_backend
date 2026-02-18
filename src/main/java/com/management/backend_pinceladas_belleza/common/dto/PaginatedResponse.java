package com.management.backend_pinceladas_belleza.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> items;
    private long total;
    private int page;
    private int pageSize;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public static <T> PaginatedResponse<T> of(List<T> items, long total, int page, int pageSize) {
        int totalPages = (int) Math.ceil((double) total / pageSize);
        
        return PaginatedResponse.<T>builder()
                .items(items)
                .total(total)
                .page(page)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .hasNext(page < totalPages)
                .hasPrevious(page > 1)
                .build();
    }
}
