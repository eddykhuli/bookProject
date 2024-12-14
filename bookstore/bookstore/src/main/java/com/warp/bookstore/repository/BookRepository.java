package com.warp.bookstore.repository;

import com.warp.bookstore.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Book findByIsbn(String isnNumber);
    Book findByTitleAndAuthor(String title, String author);
    @Query("SELECT b FROM Book b where LOWER(b.title) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :searchValue, '%'))")
    List<Book> searchBySearchValue(String searchValue);
}
