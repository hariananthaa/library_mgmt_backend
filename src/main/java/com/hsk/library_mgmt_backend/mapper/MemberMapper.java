package com.hsk.library_mgmt_backend.mapper;

import com.hsk.library_mgmt_backend.dto.MemberDto;
import com.hsk.library_mgmt_backend.persistent.entity.Member;
import com.hsk.library_mgmt_backend.web.v1.payload.member.MemberRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {
    Member toEntity(MemberRequest memberRequest);

    MemberDto toDto(Member member);

    List<MemberDto> toDto(List<Member> member);

    Member toEntity(MemberDto memberDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Member partialUpdate(MemberDto memberDto, @MappingTarget Member member);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    Member partialUpdate(Member memberDto, @MappingTarget Member member);
}