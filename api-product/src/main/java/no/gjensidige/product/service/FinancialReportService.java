package no.gjensidige.product.service;

import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import no.gjensidige.product.dto.FinancialReportDTO;
import no.gjensidige.product.entity.FinancialReport;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ReportNotFoundException;
import no.gjensidige.product.repository.FinancialReportRepository;
import no.gjensidige.product.repository.ProductRepository;

@Service
public class FinancialReportService {

    private final ProductRepository productRepository;
    private final FinancialReportRepository financialReportRepository;
    private final ModelMapper modelMapper;

    public FinancialReportService(ProductRepository productRepository,
            FinancialReportRepository financialReportRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.financialReportRepository = financialReportRepository;
        this.modelMapper = modelMapper;
    }

    public FinancialReportDTO createFinancialReport() {
        List<Product> allProducts = productRepository.findAll();

        if (allProducts.isEmpty()) {
            throw new IllegalStateException("Cannot generate report: No products found");
        }

        FinancialReport report = new FinancialReport();

        double totalRevenue = allProducts.stream().mapToDouble(this::calculateRevenue).sum();
        double totalCost = allProducts.stream().mapToDouble(this::calculateCost).sum();

        report.setTotalRevenue(totalRevenue);
        report.setTotalCost(totalCost);
        report.setTotalMargin(totalRevenue - totalCost);

        Product mostSold = allProducts.stream().max(Comparator.comparing(Product::getNumberSold))
                .orElseThrow(() -> new IllegalStateException("Cannot generate report: No products found"));
        report.setMostSoldProduct(mostSold);

        Product leastSold = allProducts.stream().min(Comparator.comparing(Product::getNumberSold))
                .orElseThrow(() -> new IllegalStateException("Cannot generate report: No products found"));
        report.setLeastSoldProduct(leastSold);

        Product highestMargin = allProducts.stream().max(Comparator.comparing(this::calculateMargin))
                .orElseThrow(() -> new IllegalStateException("Cannot generate report: No products found"));
        report.setHighestMarginProduct(highestMargin);

        Product lowestMargin = allProducts.stream().min(Comparator.comparing(this::calculateMargin))
                .orElseThrow(() -> new IllegalStateException("Cannot generate report: No products found"));
        report.setLowestMarginProduct(lowestMargin);

        return convertToDTO(financialReportRepository.save(report));

    }

    public FinancialReportDTO convertToDTO(FinancialReport financialReport) {

        FinancialReportDTO financialReportDTO = modelMapper.map(financialReport, FinancialReportDTO.class);

        return financialReportDTO;
    }

    private double calculateRevenue(Product p) {
        return p.getUnitPrice().doubleValue() * p.getNumberSold().doubleValue();
    }

    private double calculateCost(Product p) {
        return p.getUnitCost().doubleValue() * p.getNumberSold().doubleValue();
    }

    private double calculateMargin(Product p) {
        return calculateRevenue(p) - calculateCost(p);
    }

    public List<FinancialReportDTO> getAllFinancialReports() {
        return financialReportRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public FinancialReportDTO getFinancialReportById(Long id) {
        FinancialReport fr = financialReportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException(id));

        return convertToDTO(fr);
    }

}
