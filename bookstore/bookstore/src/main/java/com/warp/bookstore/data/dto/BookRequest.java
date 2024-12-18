package com.warp.bookstore.data.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String bookTitle;
    private String authorNames;
    private long bookId;
}
