package com.hsk.library_mgmt_backend.mapper;

import com.hsk.library_mgmt_backend.dto.BookTransactionDto;
import com.hsk.library_mgmt_backend.persistent.entity.BookTransaction;
import com.hsk.library_mgmt_backend.web.v1.payload.bookTransaction.BookTransactionRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BookMapper.class, MemberMapper.class})
public interface BookTransactionMapper {
    BookTransaction toEntity(BookTransactionRequest borrowedBookDto);

    BookTransactionDto toDto(BookTransaction borrowedBook);

    List<BookTransactionDto> toDto(List<BookTransaction> bookRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BookTransaction partialUpdate(BookTransactionDto borrowedBookDto, @MappingTarget BookTransaction borrowedBook);
}