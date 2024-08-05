package com.hsk.library_mgmt_backend.mapper;

import com.hsk.library_mgmt_backend.dto.BookDto;
import com.hsk.library_mgmt_backend.persistent.entity.Book;
import com.hsk.library_mgmt_backend.web.v1.payload.book.BookRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    Book toEntity(BookRequest bookRequest);
    List<Book> toEntity(List<BookRequest> bookRequestList);

    BookDto toDto(Book book);

    List<BookDto> toDto(List<Book> book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book partialUpdate(Book bookDto, @MappingTarget Book book);

    Book toEntity(BookDto bookDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book partialUpdate(BookDto bookDto, @MappingTarget Book book);
}