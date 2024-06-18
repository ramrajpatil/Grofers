package com.app.service;

import com.app.dto.Dashboard;

public interface HomeService {
	Dashboard fetchDashboardDetails(String email);
}
