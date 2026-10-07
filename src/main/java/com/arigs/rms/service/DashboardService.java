package com.arigs.rms.service;

import com.arigs.rms.dto.response.DashboardKpiResponse;
import com.arigs.rms.dto.response.MonthlyHiringResponse;
import com.arigs.rms.dto.response.RecruiterPerformanceResponse;
import com.arigs.rms.dto.response.StatusCountResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * Dashboard read-model use cases.
 */
public interface DashboardService {

    DashboardKpiResponse kpis();

    List<StatusCountResponse> candidatePipeline();

    List<StatusCountResponse> interviewPipeline();

    List<StatusCountResponse> offerPipeline();

    List<StatusCountResponse> joiningStatistics();

    List<MonthlyHiringResponse> monthlyHiring(LocalDate from, LocalDate to);

    List<RecruiterPerformanceResponse> recruiterPerformance();
}
