package com.hsk.library_mgmt_backend.service.impl;

import com.hsk.library_mgmt_backend.persistent.repository.BookRepository;
import com.hsk.library_mgmt_backend.persistent.repository.BookTransactionRepository;
import com.hsk.library_mgmt_backend.persistent.repository.MemberRepository;
import com.hsk.library_mgmt_backend.response.AdminDashboardCountResponse;
import com.hsk.library_mgmt_backend.response.MemberDashboardCountResponse;
import com.hsk.library_mgmt_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service implementation for retrieving dashboard counts for admin and members.
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final BookTransactionRepository bookTransactionRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    /**
     * Retrieves dashboard counts for the admin.
     *
     * @return an instance of {@link AdminDashboardCountResponse} containing total counts for books, borrowed books, overdue books, requested books, and members.
     */
    @Override
    public AdminDashboardCountResponse getAdminDashboardCount() {
        long totalBooks = bookRepository.countTotalBooks();
        long totalBorrowedBooks = bookTransactionRepository.countBorrowedBooks();
        long totalOverdueBooks = bookTransactionRepository.countOverdueBooks();
        long totalRequestedBooks = bookTransactionRepository.countRequestedBooks();
        long totalMembers = memberRepository.countTotalMembers();
        return new AdminDashboardCountResponse(totalBooks, totalBorrowedBooks, totalOverdueBooks, totalRequestedBooks, totalMembers);
    }

    /**
     * Retrieves dashboard counts for a specific member.
     *
     * @param memberId the ID of the member for whom the counts are to be retrieved
     * @return an instance of {@link MemberDashboardCountResponse} containing counts for books, borrowed books, overdue books, and requested books for the specified member.
     */
    @Override
    public MemberDashboardCountResponse getMemberDashboardCount(Long memberId) {
        long totalBooks = bookTransactionRepository.countBooksWithMember(memberId);
        long totalBorrowedBooks = bookTransactionRepository.countBorrowedBooksWithMember(memberId);
        long totalOverdueBooks = bookTransactionRepository.countOverdueBooksWithMember(memberId);
        long totalRequestedBooks = bookTransactionRepository.countRequestedBooksWithMember(memberId);
        return new MemberDashboardCountResponse(totalBooks, totalRequestedBooks, totalBorrowedBooks, totalOverdueBooks);
    }
}
