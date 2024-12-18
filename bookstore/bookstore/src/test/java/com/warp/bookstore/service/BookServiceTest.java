package com.warp.bookstore.service;

import com.warp.bookstore.data.dto.BookRequest;
import com.warp.bookstore.data.dto.BookResponse;
import com.warp.bookstore.data.entity.Book;
import com.warp.bookstore.exception.ParameterLengthException;
import com.warp.bookstore.repository.BookRepository;
import com.warp.bookstore.service.BookService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book mockBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock book
        mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setTitle("Test Title");
        mockBook.setAuthor("Test Author");
        mockBook.setIsbn("123456789012");
    }

    @Test
    void testSaveBook_Success() throws ParameterLengthException {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setBookTitle("Test Title");
        bookRequest.setAuthorNames("Test Author");

        when(bookRepository.findByTitleAndAuthor("Test Title", "Test Author")).thenReturn(null);
        when(bookRepository.save(any(Book.class))).thenReturn(mockBook);

        BookResponse response = bookService.saveBook(bookRequest);

        assertThat(response).isNotNull();
        assertThat(response.getBookTitle()).isEqualTo("Test Title");
        assertThat(response.getAuthorNames()).isEqualTo("Test Author");

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testSaveBook_ThrowsEntityExistsException() {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setBookTitle("Test Title");
        bookRequest.setAuthorNames("Test Author");

        when(bookRepository.findByTitleAndAuthor("Test Title", "Test Author")).thenReturn(mockBook);

        assertThrows(EntityExistsException.class, () -> bookService.saveBook(bookRequest));

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testFindByTitleAndAuthor_Success() {
        when(bookRepository.findByTitleAndAuthor("Test Title", "Test Author")).thenReturn(mockBook);

        Book book = bookService.findByTitleAndAuthor("Test Title", "Test Author");

        assertThat(book).isNotNull();
        assertThat(book.getTitle()).isEqualTo("Test Title");
        assertThat(book.getAuthor()).isEqualTo("Test Author");
    }

    @Test
    void testGetAllBooks_Success() {
        when(bookRepository.findAll()).thenReturn(List.of(mockBook));

        List<BookResponse> books = bookService.getAllBooks();

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getBookTitle()).isEqualTo("Test Title");
    }

    @Test
    void testUpdateBook_Success() throws ParameterLengthException {
        when(bookRepository.findByIsbn("123456789012")).thenReturn(mockBook);
        when(bookRepository.save(any(Book.class))).thenReturn(mockBook);

        mockBook.setTitle("Updated Title");
        BookResponse response = bookService.updateBook(mockBook);

        assertThat(response).isNotNull();
        assertThat(response.getBookTitle()).isEqualTo("Updated Title");

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testDeleteBookRecord_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        String result = bookService.deleteBookRecord(1L);

        assertThat(result).isEqualTo("Deleted Test Title By Test Author");
        verify(bookRepository, times(1)).delete(mockBook);
    }

    @Test
    void testGenerateIsbn() {
        String isbn = bookService.generateIsbn();

        assertThat(isbn).hasSize(13); // 12 digits + 1 check digit
    }

    @Test
    void testCalculateCheckValue_ValidIsbn() {
        String partialIsbn = "978030640615";
        String fullIsbn = bookService.calculateCheckValue(partialIsbn);
        assertEquals("9780306406157", fullIsbn);
    }

    @Test
    void testValidateEntity_ThrowsParameterLengthException() {
        Book book = new Book();
        book.setTitle("");
        book.setAuthor("Author");

        assertThrows(ParameterLengthException.class, () -> bookService.validateEntity(book));
    }
}
