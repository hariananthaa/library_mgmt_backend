package com.hsk.library_mgmt_backend.web.v1;

import com.hsk.library_mgmt_backend.dto.BookDto;
import com.hsk.library_mgmt_backend.helper.DashboardHelper;
import com.hsk.library_mgmt_backend.response.AdminDashboardCountResponse;
import com.hsk.library_mgmt_backend.response.MemberDashboardCountResponse;
import com.hsk.library_mgmt_backend.response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing dashboard data in the library system.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
@SecurityRequirement(name = "bearerAuth")
public class DashboardApi {

    private final DashboardHelper dashboardHelper;

    /**
     * Retrieves the dashboard count for admin users.
     *
     * @return a response containing the admin dashboard count data
     */
    @GetMapping("/admin/count")
    @PreAuthorize("hasAnyAuthority('admin:read')")
    @Operation(summary = "Get Admin Dashboard Count", tags = "Dashboard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin dashboard count retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseData<AdminDashboardCountResponse> getAdminDashboardCount() {
        return dashboardHelper.getAdminDashboardCount();
    }

    /**
     * Retrieves the dashboard count for a specific member.
     *
     * @param memberId the ID of the member whose dashboard count is to be retrieved
     * @return a response containing the member dashboard count data
     */
    @GetMapping("/member/count")
    @PreAuthorize("hasAnyAuthority('member:read')")
    @Operation(summary = "Get Member Dashboard Count", tags = "Dashboard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member dashboard count retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid member ID"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseData<MemberDashboardCountResponse> getMemberDashboardCount(
            @RequestParam("memberId") Long memberId
    ) {
        return dashboardHelper.getMemberDashboardCount(memberId);
    }
}
