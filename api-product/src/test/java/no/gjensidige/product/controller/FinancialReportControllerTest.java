package no.gjensidige.product.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import no.gjensidige.product.dto.FinancialReportDTO;
import no.gjensidige.product.exception.ReportNotFoundException;
import no.gjensidige.product.service.FinancialReportService;

@ExtendWith(MockitoExtension.class)
public class FinancialReportControllerTest {

    private MockMvc mockMvc;

    private FinancialReportController financialReportController;

    @Mock
    private FinancialReportService financialReportService;

    @BeforeEach
    public void setUp() {
        financialReportController = new FinancialReportController(financialReportService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(financialReportController)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void createFinancialReport_Success() throws Exception {
        FinancialReportDTO mockReport = createMockReportDTO(1L);
        when(financialReportService.createFinancialReport()).thenReturn(mockReport);

        mockMvc.perform(post("/reports/financial")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost/reports/financial/1"))
                .andExpect(jsonPath("$.id").value(1L));

        verify(financialReportService).createFinancialReport();
    }

    @Test
    public void createFinancialReport_NoProducts_ThrowsException() {
        when(financialReportService.createFinancialReport())
                .thenThrow(new IllegalStateException("Cannot generate report: No products found"));

        assertThrows(IllegalStateException.class, () -> {
            financialReportController.createFinancialReport();
        });

        verify(financialReportService).createFinancialReport();
    }

    @Test
    public void getFinancialReports_Success() throws Exception {
        List<FinancialReportDTO> mockReports = Arrays.asList(
                createMockReportDTO(1L),
                createMockReportDTO(2L));
        when(financialReportService.getAllFinancialReports()).thenReturn(mockReports);

        mockMvc.perform(get("/reports/financial")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(financialReportService).getAllFinancialReports();
    }

    @Test
    public void getFinancialReports_EmptyList() throws Exception {
        when(financialReportService.getAllFinancialReports()).thenReturn(List.of());

        mockMvc.perform(get("/reports/financial")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(financialReportService).getAllFinancialReports();
    }

    @Test
    public void getFinancialReportById_Success() throws Exception {
        FinancialReportDTO mockReport = createMockReportDTO(1L);
        when(financialReportService.getFinancialReportById(1L)).thenReturn(mockReport);

        mockMvc.perform(get("/reports/financial/1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(financialReportService).getFinancialReportById(1L);
    }

    @Test
    public void getFinancialReportById_NotFound_ThrowsException() {
        when(financialReportService.getFinancialReportById(999L))
                .thenThrow(new ReportNotFoundException(999L));

        assertThrows(ReportNotFoundException.class, () -> {
            financialReportController.getFinancialReportById(999L);
        });

        verify(financialReportService).getFinancialReportById(999L);
    }

    private FinancialReportDTO createMockReportDTO(Long id) {
        FinancialReportDTO dto = new FinancialReportDTO();
        dto.setId(id);
        dto.setCreated(new Timestamp(System.currentTimeMillis()));
        dto.setTotalRevenue(5000.0);
        dto.setTotalCost(2500.0);
        dto.setTotalMargin(2500.0);
        return dto;
    }
}