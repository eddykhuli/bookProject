package com.warp.bookstore.service;

import com.warp.bookstore.entity.Book;
import com.warp.bookstore.exception.ParameterLengthException;
import com.warp.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBook_ValidBook() throws ParameterLengthException {
        // Arrange
        Book book = new Book();
        book.setTitle("Valid Title");
        book.setAuthor("Author Name");
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        Book savedBook = bookService.saveBook(book);

        // Assert
        assertNotNull(savedBook, "Saved book should not be null");
        assertNotNull(savedBook.getIsbn(), "ISBN should be generated for the book");
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testSaveBook_TitleTooLong() {
        // Arrange
        Book book = new Book();
        book.setTitle("A".repeat(101)); // Title with 101 characters
        book.setAuthor("Author Name");

        // Act & Assert
        ParameterLengthException exception = assertThrows(ParameterLengthException.class, () -> {
            bookService.saveBook(book);
        });
        assertEquals("Title cannot be longer than 100 characters", exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testSaveBook_AuthorTooLong() {
        // Arrange
        Book book = new Book();
        book.setTitle("Valid Title");
        book.setAuthor("A".repeat(51)); // Author name with 51 characters

        // Act & Assert
        ParameterLengthException exception = assertThrows(ParameterLengthException.class, () -> {
            bookService.saveBook(book);
        });
        assertEquals("Author cannot be more than 50 characters", exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // Act
        List<Book> books = bookService.getAllBooks();

        // Assert
        assertNotNull(books, "Book list should not be null");
        assertEquals(2, books.size(), "Book list should contain 2 books");
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGenerateIsbn_ValidLength() {
        // Act
        String isbn = bookService.generateIsbn();

        // Assert
        assertNotNull(isbn, "Generated ISBN should not be null");
        assertEquals(13, isbn.length(), "Generated ISBN should be 13 characters long");
    }

    @Test
    void testGenerateIsbn_ValidCheckDigit() {
        // Act
        String isbn = bookService.generateIsbn();

        // Extract the first 12 digits and the check digit
        String isbnBase = isbn.substring(0, 12);
        char checkDigit = isbn.charAt(12);

        // Recalculate the check digit
        String recalculatedIsbn = bookService.calculateCheckValue(isbnBase);
        assertEquals(isbn, recalculatedIsbn, "Generated ISBN should have a valid check digit");
    }

    @Test
    void testCalculateCheckValue_ValidInput() {
        // Arrange
        String isbnBase = "978030640615";

        // Act
        String isbn = bookService.calculateCheckValue(isbnBase);

        // Assert
        assertEquals("9780306406157", isbn, "Calculated ISBN should match the expected value");
    }

    @Test
    void testCalculateCheckValue_InvalidInput() {
        // Arrange
        String invalidIsbnBase = "12345"; // Less than 12 characters

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookService.calculateCheckValue(invalidIsbnBase);
        });
        assertTrue(exception.getMessage().contains("For input string"), "Exception message should indicate invalid input");
    }
}
