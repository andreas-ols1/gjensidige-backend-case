package no.gjensidige.product.controller;

import no.gjensidige.product.dto.FinancialReportDTO;
import no.gjensidige.product.service.FinancialReportService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * RestController for the new report endpoint in exercise 2
 *
 */

@RestController
@RequestMapping("/reports")
public class FinancialReportController {

    private final FinancialReportService reportService;

    public FinancialReportController(FinancialReportService rs) {
        this.reportService = rs;
    }

    /**
     * Todo Create implementation for Financial report
     * as stated in exercise 2.
     *
     * @return
     */
    @PostMapping(value = "/financial")
    public ResponseEntity<FinancialReportDTO> createFinancialReport() {

        FinancialReportDTO report = reportService.createFinancialReport();

        URI createdReportLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(report.getId()).toUri();

        return ResponseEntity.created(createdReportLocation).body(report);
    }

    @GetMapping(value = "/financial")
    public ResponseEntity<List<FinancialReportDTO>> getFinancialReports() {
        // Implementation for retrieving financial reports can be added here
        return ResponseEntity.ok(reportService.getAllFinancialReports());
    }

    @GetMapping(value = "/financial/{id}")
    public ResponseEntity<FinancialReportDTO> getFinancialReportById(@PathVariable("id") Long id) {
        // Implementation for retrieving a specific financial report by ID can be added
        // here
        return ResponseEntity.ok(reportService.getFinancialReportById(id));
    }

}
