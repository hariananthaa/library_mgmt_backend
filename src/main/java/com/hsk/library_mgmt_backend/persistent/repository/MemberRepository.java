package com.hsk.library_mgmt_backend.persistent.repository;

import com.hsk.library_mgmt_backend.persistent.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select i from Member i where i.email = :email")
    Optional<Member> findByEmail(String email);

    @Query("SELECT COUNT(m) FROM Member m")
    long countTotalMembers();
}
