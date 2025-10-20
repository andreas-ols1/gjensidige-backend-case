package no.gjensidige.product.service;

import no.gjensidige.product.dto.FinancialReportDTO;
import no.gjensidige.product.entity.FinancialReport;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ReportNotFoundException;
import no.gjensidige.product.repository.FinancialReportRepository;
import no.gjensidige.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class FinancialReportServiceTest {

    private final ModelMapper realMapper = new ModelMapper();

    @InjectMocks
    private FinancialReportService financialReportService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FinancialReportRepository financialReportRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(modelMapper.map(any(FinancialReport.class), eq(FinancialReportDTO.class)))
                .thenAnswer(invocation -> realMapper.map(invocation.getArgument(0), FinancialReportDTO.class));
    }

    @Test
    public void createFinancialReport_Success() {
        List<Product> products = createTestProducts();
        FinancialReport savedReport = createMockReport();

        when(productRepository.findAll()).thenReturn(products);
        when(financialReportRepository.save(any(FinancialReport.class))).thenReturn(savedReport);

        FinancialReportDTO result = financialReportService.createFinancialReport();

        assertNotNull(result);
        verify(productRepository).findAll();
        verify(financialReportRepository).save(any(FinancialReport.class));

        assertTrue(result.getTotalRevenue() > 0);
        assertTrue(result.getTotalCost() > 0);
        assertTrue(result.getTotalMargin() > 0);
    }

    @Test
    public void createFinancialReport_NoProducts_ThrowsException() {
        when(productRepository.findAll()).thenReturn(List.of());

        assertThrows(IllegalStateException.class, () -> {
            financialReportService.createFinancialReport();
        });

        verify(productRepository).findAll();
        verify(financialReportRepository, never()).save(any());
    }

    @Test
    public void createFinancialReport_CalculatesCorrectTotals() {
        Product p1 = createProduct(1L, "Product 1", 100.0, 50.0, 10);
        Product p2 = createProduct(2L, "Product 2", 200.0, 100.0, 5);
        List<Product> products = Arrays.asList(p1, p2);

        FinancialReport savedReport = new FinancialReport();
        savedReport.setId(1L);
        savedReport.setTotalRevenue(2000.0); // (100*10) + (200*5)
        savedReport.setTotalCost(1000.0); // (50*10) + (100*5)
        savedReport.setTotalMargin(1000.0); // 2000 - 1000

        when(productRepository.findAll()).thenReturn(products);
        when(financialReportRepository.save(any(FinancialReport.class))).thenReturn(savedReport);

        FinancialReportDTO result = financialReportService.createFinancialReport();

        assertNotNull(result);
        assertEquals(2000.0, result.getTotalRevenue());
        assertEquals(1000.0, result.getTotalCost());
        assertEquals(1000.0, result.getTotalMargin());
    }

    @Test
    public void createFinancialReport_IdentifiesMostSoldProduct() {
        Product p1 = createProduct(1L, "Low Sales", 100.0, 50.0, 5);
        Product p2 = createProduct(2L, "High Sales", 100.0, 50.0, 20);
        Product p3 = createProduct(3L, "Medium Sales", 100.0, 50.0, 10);
        List<Product> products = Arrays.asList(p1, p2, p3);

        FinancialReport savedReport = new FinancialReport();
        savedReport.setId(1L);
        savedReport.setMostSoldProduct(p2);
        savedReport.setLeastSoldProduct(p1);

        when(productRepository.findAll()).thenReturn(products);
        when(financialReportRepository.save(any(FinancialReport.class))).thenReturn(savedReport);

        FinancialReportDTO result = financialReportService.createFinancialReport();

        assertNotNull(result);
        assertEquals(p2.getProductName(), result.getMostSoldProduct().getProductName());
        assertEquals(p1.getProductName(), result.getLeastSoldProduct().getProductName());
    }

    @Test
    public void getAllFinancialReports_Success() {
        List<FinancialReport> reports = Arrays.asList(
                createMockReport(),
                createMockReport());
        when(financialReportRepository.findAll()).thenReturn(reports);

        List<FinancialReportDTO> result = financialReportService.getAllFinancialReports();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(financialReportRepository).findAll();
    }

    @Test
    public void getAllFinancialReports_EmptyList() {
        when(financialReportRepository.findAll()).thenReturn(List.of());

        List<FinancialReportDTO> result = financialReportService.getAllFinancialReports();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(financialReportRepository).findAll();
    }

    @Test
    public void getFinancialReportById_Success() {
        FinancialReport report = createMockReport();
        when(financialReportRepository.findById(1L)).thenReturn(Optional.of(report));

        FinancialReportDTO result = financialReportService.getFinancialReportById(1L);

        assertNotNull(result);
        verify(financialReportRepository).findById(1L);
    }

    @Test
    public void getFinancialReportById_NotFound_ThrowsException() {
        when(financialReportRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ReportNotFoundException.class, () -> {
            financialReportService.getFinancialReportById(999L);
        });

        verify(financialReportRepository).findById(999L);
    }

    @Test
    public void convertToDTO_Success() {
        FinancialReport report = createMockReport();
        when(modelMapper.map(report, FinancialReportDTO.class))
                .thenReturn(realMapper.map(report, FinancialReportDTO.class));

        FinancialReportDTO result = financialReportService.convertToDTO(report);

        assertNotNull(result);
        assertEquals(report.getId(), result.getId());
        assertEquals(report.getTotalRevenue(), result.getTotalRevenue());
    }

    private List<Product> createTestProducts() {
        return Arrays.asList(
                createProduct(1L, "Product 1", 100.0, 50.0, 10),
                createProduct(2L, "Product 2", 200.0, 100.0, 5),
                createProduct(3L, "Product 3", 150.0, 75.0, 8));
    }

    private Product createProduct(Long id, String name, Double price, Double cost, int sold) {
        Product p = new Product();
        p.setId(id);
        p.setProductName(name);
        p.setUnitPrice(price);
        p.setUnitCost(cost);
        p.setNumberSold(BigInteger.valueOf(sold));
        return p;
    }

    private FinancialReport createMockReport() {
        FinancialReport report = new FinancialReport();
        report.setId(1L);
        report.setTotalRevenue(5000.0);
        report.setTotalCost(2500.0);
        report.setTotalMargin(2500.0);
        return report;
    }
}