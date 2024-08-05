package com.hsk.library_mgmt_backend.service.impl;

import com.hsk.library_mgmt_backend.dto.BookTransactionDto;
import com.hsk.library_mgmt_backend.exception.AlreadyExistingException;
import com.hsk.library_mgmt_backend.exception.NotFoundException;
import com.hsk.library_mgmt_backend.mapper.BookTransactionMapper;
import com.hsk.library_mgmt_backend.persistent.entity.Book;
import com.hsk.library_mgmt_backend.persistent.entity.BookTransaction;
import com.hsk.library_mgmt_backend.persistent.entity.Member;
import com.hsk.library_mgmt_backend.persistent.repository.BookTransactionRepository;
import com.hsk.library_mgmt_backend.service.BookService;
import com.hsk.library_mgmt_backend.service.BookTransactionService;
import com.hsk.library_mgmt_backend.service.MemberService;
import com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction.BookTransactionRequest;
import com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction.BookTransactionUpdateRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing book transactions in the library management system.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookTransactionServiceImpl implements BookTransactionService {

    private final BookTransactionRepository bookTransactionRepository;
    private final BookService bookService;
    private final MemberService memberService;
    private final BookTransactionMapper bookTransactionMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * Adds a new book transaction.
     *
     * @param bookTransactionRequest the request containing details for the new book transaction
     * @return the added book transaction
     * @throws AlreadyExistingException if a book transaction with the same book ID and member ID already exists
     */
    @Override
    @Transactional
    public BookTransaction addBookTransaction(BookTransactionRequest bookTransactionRequest) {
        BookTransaction existing = bookTransactionRepository.findByBookIdAndMemberId(
                bookTransactionRequest.bookId(), bookTransactionRequest.memberId()).orElse(null);

        if (existing != null) throw new AlreadyExistingException("BookTransaction already present");

        Book requestedBook = bookService.getBookById(bookTransactionRequest.bookId());
        Member member = memberService.getMemberById(bookTransactionRequest.memberId());

        existing = new BookTransaction();
        existing.setRequestDate(LocalDate.now());
        existing.setStatus(BookTransaction.Status.REQUESTED);
        existing.setBook(requestedBook);
        existing.setMember(member);
        existing = bookTransactionRepository.save(existing);

        requestedBook.setCopiesAvailable(requestedBook.getCopiesAvailable() - 1);
        bookService.updateBookById(bookTransactionRequest.bookId(), requestedBook);

        return existing;
    }

    /**
     * Updates an existing book transaction by its ID.
     *
     * @param id the ID of the book transaction to be updated
     * @param updateRequest the request containing update details
     * @return the updated book transaction
     * @throws NotFoundException if the book transaction with the given ID is not found
     */
    @Override
    @Transactional
    public BookTransaction updateBookTransactionById(Long id, BookTransactionUpdateRequest updateRequest) {
        BookTransaction bookTransaction = bookTransactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BookTransaction not found"));

        Book requestedBook = bookTransaction.getBook();

        switch (updateRequest.status()) {
            case REQUESTED -> bookTransaction.setRequestDate(LocalDate.now());
            case APPROVED -> {
                bookTransaction.setIssueDate(LocalDate.now());
                bookTransaction.setDueDate(updateRequest.dueDate());
                requestedBook.setCopiesAvailable(requestedBook.getCopiesAvailable() - 1);
            }
            case RETURNED -> {
                bookTransaction.setReturnDate(updateRequest.returnDate());
                requestedBook.setCopiesAvailable(requestedBook.getCopiesAvailable() + 1);
            }
        }

        bookTransaction.setStatus(updateRequest.status());
        bookTransaction = bookTransactionRepository.saveAndFlush(bookTransaction);
        bookService.updateBookById(requestedBook.getId(), requestedBook);

        return bookTransaction;
    }

    /**
     * Deletes a book transaction by its ID.
     *
     * @param id the ID of the book transaction to be deleted
     * @throws NotFoundException if the book transaction with the given ID is not found
     */
    @Override
    @Transactional
    public void deleteBookTransactionById(Long id) {
        bookTransactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BookTransaction not found"));
        bookTransactionRepository.deleteById(id);
    }

    /**
     * Retrieves a book transaction by its ID.
     *
     * @param id the ID of the book transaction to be retrieved
     * @return the retrieved book transaction
     * @throws NotFoundException if the book transaction with the given ID is not found
     */
    @Override
    public BookTransaction getBookTransactionById(Long id) {
        return bookTransactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BookTransaction not found"));
    }

    /**
     * Retrieves all book transactions with pagination and optional filters.
     *
     * @param pageable the pagination information
     * @param queryString the search query to filter transactions by book title or member name
     * @param status the status to filter transactions
     * @param memberId the member ID to filter transactions
     * @return a page of book transactions matching the filters
     */
    @Override
    public Page<BookTransactionDto> getAllBookTransaction(Pageable pageable, String queryString, String status, Long memberId) {

        // Get the CriteriaBuilder from EntityManager
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // Create a CriteriaQuery for BookTransaction entities
        CriteriaQuery<BookTransaction> query = criteriaBuilder.createQuery(BookTransaction.class);

        // Set the root entity for the query
        Root<BookTransaction> root = query.from(BookTransaction.class);

        // List to store the conditions for the WHERE clause
        List<Predicate> predicates = new ArrayList<>();

        if (!queryString.isEmpty()) {
            Predicate searchPredicate;
            Predicate memberNamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("member").get("name")),
                    "%" + queryString.toLowerCase() + '%');

            Predicate bookNamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("book").get("title")),
                    "%" + queryString.toLowerCase() + '%');
            searchPredicate = criteriaBuilder.or(memberNamePredicate, bookNamePredicate);
            predicates.add(searchPredicate);
        }

        if (!status.isEmpty()) {
            Predicate statusPredicate = criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("status")),
                    status.toLowerCase());
            predicates.add(statusPredicate);
        }
        if (memberId != null) {
            Predicate memberPredicate = criteriaBuilder.equal(
                    root.get("member").get("id"), memberId);
            predicates.add(memberPredicate);
        }

        // Apply the conditions to the WHERE clause
        query.where(predicates.toArray(new Predicate[0]));

        // Add ORDER BY clauses
        query.orderBy(
                criteriaBuilder.desc(root.get("updatedAt")) // Order by updatedAt DESC
        );

        // Execute the query and retrieve the list of book transactions
        TypedQuery<BookTransaction> typedQuery = entityManager.createQuery(query);

        // Count total records without pagination
        int totalCount = typedQuery.getResultList().size();
        log.debug("Total bookTransaction count: {}", totalCount);

        // Apply pagination
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<BookTransaction> bookTransactions = typedQuery.getResultList();

        // Convert the book transactions to DTOs
        List<BookTransactionDto> bookTransactionDtoList = bookTransactionMapper.toDto(bookTransactions);

        // Create a Page object
        log.debug("Returning paginated bookTransaction results.");

        return new PageImpl<>(bookTransactionDtoList, pageable, totalCount);
    }

    /**
     * Retrieves all overdue book transactions for a specific member.
     *
     * @param pageable the pagination information
     * @param memberId the member ID to filter transactions
     * @return a page of overdue book transactions for the specified member
     */
    @Override
    public Page<BookTransactionDto> getAllOverdueBookTransactions(Pageable pageable, Long memberId) {
        Page<BookTransaction> bookTransactionPage = bookTransactionRepository.findOverdueBooksByMember(memberId, pageable);
        List<BookTransactionDto> bookTransactionDtoPage = bookTransactionMapper.toDto(bookTransactionPage.getContent());
        return new PageImpl<>(bookTransactionDtoPage, pageable, bookTransactionDtoPage.size());
    }
}
