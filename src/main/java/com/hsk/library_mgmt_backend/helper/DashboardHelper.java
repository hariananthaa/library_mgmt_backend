package com.hsk.library_mgmt_backend.helper;

import com.hsk.library_mgmt_backend.dto.BookDto;
import com.hsk.library_mgmt_backend.response.AdminDashboardCountResponse;
import com.hsk.library_mgmt_backend.response.MemberDashboardCountResponse;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.response.ResponseUtil;
import com.hsk.library_mgmt_backend.service.BookService;
import com.hsk.library_mgmt_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardHelper {
    private final DashboardService dashboardService;
    public ResponseData<AdminDashboardCountResponse> getAdminDashboardCount() {
        AdminDashboardCountResponse response = dashboardService.getAdminDashboardCount();
        return ResponseUtil.responseConverter(response);
    }

    public ResponseData<MemberDashboardCountResponse> getMemberDashboardCount(Long memberId) {
        MemberDashboardCountResponse response = dashboardService.getMemberDashboardCount(memberId);
        return ResponseUtil.responseConverter(response);
    }
}
