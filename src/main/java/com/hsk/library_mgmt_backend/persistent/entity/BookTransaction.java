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
@Table(name = "book_transaction")
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "book_transaction_seq", allocationSize = 1)
public class BookTransaction extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -5576653075671844783L;
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_transaction_seq")
    @Id
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "book_fk",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_BOOK_TRANSACTION_ON_BOOK")
    )
    private Book book;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "member_fk",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_BOOK_TRANSACTION_ON_MEMBER")
    )
    private Member member;

    @Column(nullable = false, name = "request_date")
    private LocalDate requestDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookTransaction.Status status;
    @Column(name = "issue_date")
    private LocalDate issueDate;
    @Column(name = "due_date")
    private LocalDate dueDate;
    @Column(name = "return_date")
    private LocalDate returnDate;

    public enum Status {
        REQUESTED,
        APPROVED,
        RETURNED,
        CANCELLED
    }
}

