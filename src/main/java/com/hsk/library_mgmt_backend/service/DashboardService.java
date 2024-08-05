package com.hsk.library_mgmt_backend.service;

import com.hsk.library_mgmt_backend.response.AdminDashboardCountResponse;
import com.hsk.library_mgmt_backend.response.MemberDashboardCountResponse;

public interface DashboardService {
   AdminDashboardCountResponse getAdminDashboardCount();

    MemberDashboardCountResponse getMemberDashboardCount(Long memberId);
}
