package com.hsk.library_mgmt_backend.service.impl;

import com.hsk.library_mgmt_backend.config.JWTService;
import com.hsk.library_mgmt_backend.dto.MemberDto;
import com.hsk.library_mgmt_backend.exception.AlreadyExistingException;
import com.hsk.library_mgmt_backend.exception.NotFoundException;
import com.hsk.library_mgmt_backend.mapper.MemberMapper;
import com.hsk.library_mgmt_backend.persistent.entity.Member;
import com.hsk.library_mgmt_backend.persistent.repository.BookTransactionRepository;
import com.hsk.library_mgmt_backend.persistent.repository.MemberRepository;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.response.ResponseUtil;
import com.hsk.library_mgmt_backend.service.MemberService;
import com.hsk.library_mgmt_backend.web.v1.payload.member.MemberRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link MemberService} interface for managing members.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookTransactionRepository bookTransactionRepository;
    private final JWTService jwtService;
    private final MemberMapper memberMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * Adds a new member to the system.
     *
     * @param member the details of the member to be added
     * @return a {@link ResponseData} containing the JWT token for the new member
     * @throws AlreadyExistingException if a member with the given email already exists
     */
    @Override
    @Transactional
    public Member addMember(MemberRequest member) {
        Member existing = memberRepository.findByEmail(member.email()).orElse(null);
        if (existing != null) throw new AlreadyExistingException("Member already present");
        existing = new Member();
        existing.setEmail(member.email());
        existing.setName(member.name());
        existing.setPhone(member.phone());
        existing.setPassword(passwordEncoder.encode(member.password()));
        existing.setRole(member.role());
        existing = memberRepository.save(existing);
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("name", existing.getName());
//        claims.put("id", existing.getId());
//        claims.put("phone", existing.getPhone());
//        claims.put("role", existing.getRole());
//        var jwtToken = jwtService.generateToken(existing, claims);
//        return ResponseUtil.responseConverter(jwtToken, 201);
        return existing;
    }

    /**
     * Retrieves a member by their email address.
     *
     * @param email the email of the member to be retrieved
     * @return the {@link Member} with the given email or null if not found
     */
    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    /**
     * Updates the details of an existing member.
     *
     * @param id     the ID of the member to be updated
     * @param member the new details of the member
     * @return the updated {@link Member}
     * @throws NotFoundException if the member with the given ID is not found
     */
    @Override
    @Transactional
    public Member updateMemberById(Long id, MemberRequest member) {
        Member toBeUpdatedMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found"));
        toBeUpdatedMember.setName(member.name());
        toBeUpdatedMember.setEmail(member.email());
        toBeUpdatedMember.setPhone(member.phone());
        toBeUpdatedMember.setPassword(passwordEncoder.encode(member.password()));
        toBeUpdatedMember.setRole(member.role());

        return memberRepository.saveAndFlush(toBeUpdatedMember);
    }

    /**
     * Deletes a member by their ID.
     *
     * @param id the ID of the member to be deleted
     * @throws NotFoundException if the member with the given ID is not found
     */
    @Override
    @Transactional
    public void deleteMemberById(Long id) {
        bookTransactionRepository.deleteByMemberId(id);

        memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found"));
        memberRepository.deleteById(id);
    }

    /**
     * Retrieves a member by their ID.
     *
     * @param id the ID of the member to be retrieved
     * @return the {@link Member} with the given ID
     * @throws NotFoundException if the member with the given ID is not found
     */
    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found"));
    }

    /**
     * Retrieves all members with pagination and search functionality.
     *
     * @param pageable    the pagination information
     * @param queryString the search query to filter members
     * @return a {@link Page} of {@link MemberDto} matching the search criteria
     */
    @Override
    public Page<MemberDto> getAllMember(Pageable pageable, String queryString) {

        // Get the CriteriaBuilder from EntityManager
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // Create a CriteriaQuery for Member entities
        CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);

        // Set the root entity for the query
        Root<Member> root = query.from(Member.class);

        // List to store the conditions for the WHERE clause
        List<Predicate> predicates = new ArrayList<>();

        // Combined search predicate (OR logic across multiple fields)
        Predicate searchPredicate = null;
        for (String field : new String[]{"name", "phone", "email", "role"}) {
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

        // Apply the conditions to the WHERE clause
        query.where(predicates.toArray(new Predicate[0]));

        // Add ORDER BY clauses
        query.orderBy(
                criteriaBuilder.desc(root.get("updatedAt")) // Then order by createdAt DESC
        );

        // Execute the query and retrieve the list of members
        TypedQuery<Member> typedQuery = entityManager.createQuery(query);

        // Count total records without pagination
        int totalCount = typedQuery.getResultList().size();
        log.debug("Total member count: {}", totalCount);

        // Apply pagination
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Member> members = typedQuery.getResultList();

        // Convert the members to DTOs
        List<MemberDto> memberDtoList = memberMapper.toDto(members);

        // Create a Page object
        log.debug("Returning paginated member results.");

        return new PageImpl<>(memberDtoList, pageable, totalCount);
    }
}
