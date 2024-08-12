package com.hsk.library_mgmt_backend.helper;

import com.hsk.library_mgmt_backend.dto.MemberDto;
import com.hsk.library_mgmt_backend.mapper.MemberMapper;
import com.hsk.library_mgmt_backend.persistent.entity.Member;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.response.ResponsePaginationData;
import com.hsk.library_mgmt_backend.response.ResponsePaginationUtil;
import com.hsk.library_mgmt_backend.response.ResponseUtil;
import com.hsk.library_mgmt_backend.service.MemberService;
import com.hsk.library_mgmt_backend.web.v1.payload.member.MemberRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberHelper {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public ResponseData<MemberDto> addMember(MemberRequest memberRequest, BindingResult bindingResult) {
        BindingResultHelper.processBindingResult(bindingResult);
        Member member =  memberService.addMember(memberRequest);
        return ResponseUtil.responseConverter(memberMapper.toDto(member));
    }

    public ResponseData<MemberDto> getMemberById(Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseUtil.responseConverter(memberMapper.toDto(member));
    }

    public ResponsePaginationData<List<MemberDto>> getAllMembers(Pageable pageable, @Valid String query) {
        Page<MemberDto> memberDtoPage = memberService.getAllMember(pageable, query);
        return ResponsePaginationUtil.responsePaginationConverter(memberDtoPage.getContent(),
                memberDtoPage.getTotalElements(),
                memberDtoPage.getTotalPages(),
                memberDtoPage.getNumber());
    }

    public ResponseData<MemberDto> updateMemberById(Long id, MemberRequest memberRequest, BindingResult bindingResult) {
        BindingResultHelper.processBindingResult(bindingResult);
        Member member = memberService.updateMemberById(id, memberRequest);
        return ResponseUtil.responseConverter(memberMapper.toDto(member));
    }

    public ResponseData<Object> deleteMemberById(Long id) {
        memberService.deleteMemberById(id);
        return ResponseUtil.responseConverter(null);
    }
}
