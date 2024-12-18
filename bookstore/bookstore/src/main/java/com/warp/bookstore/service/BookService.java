package com.warp.bookstore.service;

import com.warp.bookstore.data.dto.BookRequest;
import com.warp.bookstore.data.dto.BookResponse;
import com.warp.bookstore.data.entity.Book;
import com.warp.bookstore.exception.ParameterLengthException;
import com.warp.bookstore.helper.BookMapper;
import com.warp.bookstore.repository.BookRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class BookService {
    private static final int ISBN_SIZE =12;
    @Autowired
    private BookRepository bookRepository;

    public BookResponse saveBook(BookRequest request) throws ParameterLengthException,EntityExistsException {
        Book book = BookMapper.fromBookRequest(request);
        validateEntity(book);
        book.setIsbn(generateIsbn());
        return BookMapper.toBookResponse(bookRepository.save(book));
    }

    public Book findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }

    public List<BookResponse> getAllBooks() {
        List<BookResponse> books = new ArrayList<>();
        for( Book book : bookRepository.findAll()) {
            books.add(BookMapper.toBookResponse(book));
        }
        return books;
    }

    public List<BookResponse> searchBySearchField(String searchValue) {
        List<Book> books = bookRepository.searchBySearchValue(searchValue);
        List<BookResponse> response = new ArrayList<>();
        for (Book book : books) {
            response.add(BookMapper.toBookResponse(book));
        }
        return response;
    }

    public BookResponse updateBook(Book updated) throws ParameterLengthException {
        Book book = findBookByIsbn(updated.getIsbn());
        if (book == null) {
            throw new EntityNotFoundException("Book not found");
        }
        book.setAuthor(updated.getAuthor());
        book.setTitle(updated.getTitle());
        checkFieldsLength(book);
       return BookMapper.toBookResponse(bookRepository.save(book));
    }

    public Book findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public String deleteBookRecord(Long id) {
        Book toDelete = bookRepository.findById(id).get();
        bookRepository.delete(toDelete);
        return "Deleted "+toDelete.getTitle()+" By "+toDelete.getAuthor();
    }

    public BookResponse findById(Long id) {
        return BookMapper.toBookResponse(bookRepository.findById(id).orElse(new Book()));
    }

    public String generateIsbn() {

        Random random = new Random();
        StringBuilder preIsbn = new StringBuilder();
        for (int x =0; x < ISBN_SIZE; x++) {
            preIsbn.append(random.nextInt(10));
        }
        return calculateCheckValue(preIsbn.toString());
    }

    public String calculateCheckValue(String isbn) {
        int isbnSum = 0;
        int checkValue = 0;
        char[] numbers = isbn.toCharArray();
        for (int x = 0; x < numbers.length; x++) {
            int weight = x % 2 == 0 ? 1 : 3;
            isbnSum += Integer.parseInt(String.valueOf(numbers[x])) * weight;
        }
        int remainder = isbnSum % 10;
        if (remainder != 0) {
            checkValue = 10 - remainder;
        }
        isbn += checkValue;
        return isbn;
    }

    public void validateEntity(Book book) throws ParameterLengthException,EntityExistsException {
        checkFieldsLength(book);
        Book exists = findByTitleAndAuthor(book.getTitle(),book.getAuthor());
        if (exists != null) {
            throw new EntityExistsException("Book already exists");
        }
    }

    public void checkFieldsLength(Book book) throws ParameterLengthException {
        if (book.getTitle().isBlank() || book.getTitle().isEmpty() || book.getAuthor().isBlank() || book.getAuthor().isEmpty()) {
            throw new ParameterLengthException("Title and Author names cannot be blank!");
        }

        if (book.getTitle().length() > 100) {
            throw new ParameterLengthException("Title cannot be longer than 100 characters!");
        }

        if (book.getAuthor().length() > 50) {
            throw new ParameterLengthException("Author cannot be more than 50 characters!");
        }
    }

}
