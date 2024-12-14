package com.warp.bookstore.dto;

import lombok.Builder;

@Builder
public class BookDto {
    private String title;
    private String authorName;
    private String authorLastName;
    private String isbn;
    private long id;
}
