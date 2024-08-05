package com.hsk.library_mgmt_backend.persistent.repository;

import com.hsk.library_mgmt_backend.persistent.entity.BookTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookTransactionRepository extends JpaRepository<BookTransaction, Long> {
    @Query("select i from BookTransaction i where i.book.id = :bookId AND i.member.id = :memberId")
    Optional<BookTransaction> findByBookIdAndMemberId(Long bookId, Long memberId);

    @Modifying
    @Query("delete from BookTransaction i where i.member.id = :memberId")
    void deleteByMemberId(Long memberId);

    @Modifying
    @Query("delete from BookTransaction i where i.book.id = :bookId")
    void deleteByBookId(Long bookId);

    @Query("SELECT COUNT(bt) FROM BookTransaction bt WHERE bt.returnDate IS NULL AND bt.status = 'APPROVED'")
    long countBorrowedBooks();

    @Query("SELECT COUNT(bt) FROM BookTransaction bt WHERE bt.dueDate < CURRENT_DATE AND bt.returnDate IS NULL AND bt.status != 'CANCELLED'")
    long countOverdueBooks();

    @Query("SELECT COUNT(br) FROM BookTransaction br WHERE br.status = 'REQUESTED'")
    long countRequestedBooks();

    @Query("SELECT COUNT(bt) FROM BookTransaction bt WHERE bt.returnDate IS NULL AND bt.member.id = :memberId AND bt.status = 'APPROVED'")
    long countBorrowedBooksWithMember(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(bt) FROM BookTransaction bt WHERE bt.dueDate < CURRENT_DATE AND bt.returnDate IS NULL AND bt.member.id = :memberId AND bt.status != 'CANCELLED'")
    long countOverdueBooksWithMember(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(bt) FROM BookTransaction bt WHERE bt.status = 'REQUESTED' AND bt.member.id = :memberId")
    long countRequestedBooksWithMember(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(bt) FROM BookTransaction bt WHERE bt.member.id = :memberId")
    long countBooksWithMember(@Param("memberId") Long memberId);

    @Query("SELECT bt FROM BookTransaction bt WHERE bt.dueDate < CURRENT_DATE AND bt.returnDate IS NULL AND bt.member.id = :memberId AND bt.status != 'CANCELLED'")
    Page<BookTransaction> findOverdueBooksByMember(@Param("memberId") Long memberId, Pageable pageable);
}
