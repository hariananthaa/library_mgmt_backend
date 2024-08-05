package com.hsk.library_mgmt_backend.service.impl;

import com.hsk.library_mgmt_backend.dto.BookDto;
import com.hsk.library_mgmt_backend.exception.AlreadyExistingException;
import com.hsk.library_mgmt_backend.exception.NotFoundException;
import com.hsk.library_mgmt_backend.mapper.BookMapper;
import com.hsk.library_mgmt_backend.persistent.entity.Book;
import com.hsk.library_mgmt_backend.persistent.repository.BookRepository;
import com.hsk.library_mgmt_backend.persistent.repository.BookTransactionRepository;
import com.hsk.library_mgmt_backend.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing books in the library management system.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookTransactionRepository bookTransactionRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * Adds a new book to the repository.
     *
     * @param book the book to be added
     * @return the added book
     * @throws AlreadyExistingException if the book with the same ISBN already exists
     */
    @Override
    @Transactional
    public Book addBook(Book book) {
        Book existing = bookRepository.findByIsbn(book.getIsbn());
        if (existing != null) throw new AlreadyExistingException("Book already present");
        return bookRepository.save(book);
    }

    /**
     * Updates an existing book by its ID.
     *
     * @param id the ID of the book to be updated
     * @param book the new book details
     * @return the updated book
     * @throws NotFoundException if the book with the given ID is not found
     */
    @Override
    @Transactional
    public Book updateBookById(Long id, Book book) {
        Book toBeUpdatedBook = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        toBeUpdatedBook = bookMapper.partialUpdate(book, toBeUpdatedBook);

        return bookRepository.saveAndFlush(toBeUpdatedBook);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to be deleted
     * @throws NotFoundException if the book with the given ID is not found
     */
    @Override
    @Transactional
    public void deleteBookById(Long id) {
        bookTransactionRepository.deleteByBookId(id);

        bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        bookRepository.deleteById(id);
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to be retrieved
     * @return the retrieved book
     * @throws NotFoundException if the book with the given ID is not found
     */
    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    /**
     * Retrieves all books with pagination and optional query and genre filters.
     *
     * @param pageable the pagination information
     * @param queryString the search query to filter books
     * @param genre the genre to filter books
     * @return a page of books matching the filters
     */
    @Override
    public Page<BookDto> getAllBook(Pageable pageable, String queryString, String genre) {

        // Get the CriteriaBuilder from EntityManager
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // Create a CriteriaQuery for Book entities
        CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);

        // Set the root entity for the query
        Root<Book> root = query.from(Book.class);

        // List to store the conditions for the WHERE clause
        List<Predicate> predicates = new ArrayList<>();

        if (!queryString.isEmpty()) {
            // Combined search predicate (OR logic across multiple fields)
            Predicate searchPredicate = null;
            for (String field : new String[]{"title", "author", "isbn", "genre"}) {
                Predicate fieldPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(field)),
                        "%" + queryString.toLowerCase() + '%');
                if (searchPredicate == null) {
                    searchPredicate = fieldPredicate;
                } else {
                    searchPredicate = criteriaBuilder.or(searchPredicate, fieldPredicate);
                }
            }
            predicates.add(searchPredicate);
        }

        if (!genre.isEmpty()) {
            Predicate genrePredicate = criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("genre")),
                    genre.toLowerCase());
            predicates.add(genrePredicate);
        }

        // Apply the conditions to the WHERE clause
        query.where(predicates.toArray(new Predicate[0]));

        // Add ORDER BY clauses
        query.orderBy(
                criteriaBuilder.desc(root.get("updatedAt")) // Then order by createdAt DESC
        );

        // Execute the query and retrieve the list of books
        TypedQuery<Book> typedQuery = entityManager.createQuery(query);

        // Count total records without pagination
        int totalCount = typedQuery.getResultList().size();
        log.debug("Total book count: {}", totalCount);

        // Apply pagination
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Book> books = typedQuery.getResultList();

        // Convert the books to DTOs
        List<BookDto> bookDtoList = bookMapper.toDto(books);

        // Create a Page object
        log.debug("Returning paginated book results.");

        return new PageImpl<>(bookDtoList, pageable, totalCount);
    }

    /**
     * Adds a list of books to the repository.
     *
     * @param bookList the list of books to be added
     * @return the list of added books
     */
    @Override
    @Transactional
    public List<Book> addBookBulky(List<Book> bookList) {
        List<Book> updated = new ArrayList<>();
        for (Book book : bookList) {
            updated.add(addBook(book));
        }
        return updated;
    }
}
