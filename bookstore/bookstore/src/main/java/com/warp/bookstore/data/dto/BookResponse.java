package com.warp.bookstore.data.dto;

import lombok.Data;

@Data
public class BookResponse {
    private String bookTitle;
    private String authorNames;
    private String isbnNumber;
    private long bookId;
}
