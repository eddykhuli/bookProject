package com.warp.bookstore.helper;

import com.warp.bookstore.data.dto.BookRequest;
import com.warp.bookstore.data.dto.BookResponse;
import com.warp.bookstore.data.entity.Book;

public class BookMapper {

    public static Book fromBookRequest(BookRequest dto) {
        return new Book(dto.getBookTitle(),dto.getAuthorNames());
    }

    public static BookResponse toBookResponse(Book book) {
        BookResponse dto = new BookResponse();
        dto.setBookId(book.getId() != null ? book.getId() : -1);
        dto.setBookTitle(book.getTitle());
        dto.setAuthorNames(book.getAuthor());
        dto.setIsbnNumber(book.getIsbn());
        return dto;
    }
}
