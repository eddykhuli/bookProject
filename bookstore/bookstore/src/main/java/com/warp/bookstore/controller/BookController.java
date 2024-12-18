package com.warp.bookstore.controller;

import com.warp.bookstore.data.dto.BookRequest;
import com.warp.bookstore.data.dto.BookResponse;
import com.warp.bookstore.data.entity.Book;
import com.warp.bookstore.exception.ParameterLengthException;
import com.warp.bookstore.helper.BookMapper;
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

    @PostMapping()
    public ResponseEntity<BookResponse> saveBook(@RequestBody BookRequest book) throws ParameterLengthException,EntityExistsException {
        return new ResponseEntity<>(bookService.saveBook(book), HttpStatus.OK) ;
    }

    @GetMapping("{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findById(id));
    }

    @GetMapping("search-book/{searchValue}")
    public ResponseEntity<List<BookResponse>> searchBook(@PathVariable("searchValue") String searchValue) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.searchBySearchField(searchValue));
    }

    @GetMapping("get-books")
    public ResponseEntity<List<BookResponse>> getBooks() {
       return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    @DeleteMapping("delete-book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") Long id) throws EntityNotFoundException{
        return ResponseEntity.status(HttpStatus.OK).body(bookService.deleteBookRecord(id));
    }

    @PostMapping("update-book")
    public ResponseEntity<BookResponse> updateBook(@RequestBody Book book) throws EntityNotFoundException,ParameterLengthException {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(book));
    }

}
