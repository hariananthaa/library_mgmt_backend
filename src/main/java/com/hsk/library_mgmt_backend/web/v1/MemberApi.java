package com.hsk.library_mgmt_backend.web.v1;

import com.hsk.library_mgmt_backend.dto.MemberDto;
import com.hsk.library_mgmt_backend.helper.MemberHelper;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.response.ResponsePaginationData;
import com.hsk.library_mgmt_backend.web.v1.payload.member.MemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing members in the library system.
 */
@RestController
@RequestMapping("/api/v1/member")
@SecurityRequirement(name = "bearerAuth")
public class MemberApi {
    private final MemberHelper memberHelper;

    @Autowired
    public MemberApi(MemberHelper memberHelper) {
        this.memberHelper = memberHelper;
    }

    /**
     * Adds a new member to the system.
     *
     * @param request the request containing member details
     * @param bindingResult validation results for the request
     * @return a response containing the result of the addition operation
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create')")
    @Operation(summary = "Add a New Member", tags = "Member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseData<String> addMember(@Valid @RequestBody MemberRequest request,
                                          BindingResult bindingResult) {
        return memberHelper.addMember(request, bindingResult);
    }

    /**
     * Retrieves a member by their ID.
     *
     * @param id the ID of the member to retrieve
     * @return a response containing the member's details
     */
    @GetMapping("/{memberId}")
    @PreAuthorize("hasAnyAuthority('admin:read','member:read')")
    @Operation(summary = "Get Member by ID", tags = "Member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseData<MemberDto> getMemberById(@PathVariable("memberId") Long id) {
        return memberHelper.getMemberById(id);
    }

    /**
     * Retrieves a paginated list of members based on a search query.
     *
     * @param query the search query to filter members
     * @param pageNumber the page number for pagination
     * @param pageSize the number of members per page
     * @return a paginated response containing a list of members
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('admin:read')")
    @Operation(summary = "Get All Members", tags = "Member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Members retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or query parameters"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponsePaginationData<List<MemberDto>> getAllMembers(
            @Valid @RequestParam(value = "query", defaultValue = "", required = false) String query,
            @Valid @RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNumber,
            @Valid @RequestParam(value = "size", defaultValue = "10", required = false) Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return memberHelper.getAllMembers(pageable, query);
    }

    /**
     * Updates the details of an existing member.
     *
     * @param id the ID of the member to update
     * @param request the request containing updated member details
     * @param bindingResult validation results for the request
     * @return a response containing the updated member's details
     */
    @PutMapping("/{memberId}")
    @PreAuthorize("hasAnyAuthority('admin:update')")
    @Operation(summary = "Update Member by ID", tags = "Member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseData<MemberDto> updateMemberById(@PathVariable("memberId") Long id,
                                                    @Valid @RequestBody MemberRequest request, BindingResult bindingResult) {
        return memberHelper.updateMemberById(id, request, bindingResult);
    }

    /**
     * Deletes a member by their ID.
     *
     * @param id the ID of the member to delete
     * @return a response indicating the result of the deletion operation
     */
    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasAnyAuthority('admin:delete')")
    @Operation(summary = "Delete Member by ID", tags = "Member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseData<Object> deleteMemberById(@PathVariable("memberId") Long id) {
        return memberHelper.deleteMemberById(id);
    }
}
