package com.hustleborn.service.controller.reports;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hustleborn.service.model.reports.ProfitReport;
import com.hustleborn.service.service.reports.ReportingService;
import com.hustleborn.service.utils.responses.ApiResponse;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    @GetMapping("/profit")
    public ResponseEntity<ApiResponse> getProfitReport() {
        ProfitReport report = reportingService.getProfitReport();
        Map<String, Object> data = new HashMap<>();
        data.put("profitReport", report);
        ApiResponse response = new ApiResponse(true, "Profit report retrieved successfully", data);
        return ResponseEntity.ok(response);
    }
}