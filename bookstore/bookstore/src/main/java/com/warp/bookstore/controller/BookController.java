package com.warp.bookstore.controller;

import com.warp.bookstore.entity.Book;
import com.warp.bookstore.exception.ParameterLengthException;
import com.warp.bookstore.service.BookService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/book/")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("addBook")
    public ResponseEntity saveBook(@RequestBody Book book) {

        try {
            Book book1 = bookService.saveBook(book);
            return new ResponseEntity<>(book1, HttpStatus.OK) ;
        } catch (ParameterLengthException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (EntityExistsException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    @GetMapping("find-book")
    public ResponseEntity<Book> getBook(@RequestParam String title,@RequestParam String author) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findByTitleAndAuthor(title,author));
    }

    @GetMapping("search-book")
    public ResponseEntity<List<Book>> searchBook(@RequestParam String searchValue) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.searchBysearchField(searchValue));
    }

    @GetMapping("get-books")
    public ResponseEntity<List<Book>> getBooks() {
       return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    @DeleteMapping("delete-book")
    public ResponseEntity<String> deleteBook(@RequestParam String isbn) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(bookService.deleteBookRecord(isbn));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
        }
    }

    @PostMapping("update-book")
    public ResponseEntity updatseBook(@RequestBody Book book) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(book));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
        }
    }

    @GetMapping("testIsbn")
    public String getIsbn(){
        return bookService.calculateCheckValue("978030640615");
    }
}
