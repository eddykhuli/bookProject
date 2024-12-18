package com.warp.bookstore.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 100)
    @NonNull
    private String title;
    @Column(nullable = false, length = 50)
    @NonNull
    private String author;
    @Column(unique = true, length = 13)
    private String isbn;

}
