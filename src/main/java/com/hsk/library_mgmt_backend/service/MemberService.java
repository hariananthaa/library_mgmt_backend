package com.hsk.library_mgmt_backend.service;


import com.hsk.library_mgmt_backend.dto.MemberDto;
import com.hsk.library_mgmt_backend.persistent.entity.Member;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.web.v1.payload.member.MemberRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    Member addMember(MemberRequest member);

    Member getMemberByEmail(String email);

    Member updateMemberById(Long id, MemberRequest member);

    void deleteMemberById(Long id);

    Member getMemberById(Long id);

    Page<MemberDto> getAllMember(Pageable pageable, String query);
}
