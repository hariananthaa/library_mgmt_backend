package com.hsk.library_mgmt_backend.persistent.entity;

import com.hsk.library_mgmt_backend.persistent.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Audited
@Table(name = "book")
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "book_seq", allocationSize = 1)
public class Book extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -5314257913701046308L;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @Id
    Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    private String genre;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(nullable = false, name = "copies_available")
    private int copiesAvailable;
}
