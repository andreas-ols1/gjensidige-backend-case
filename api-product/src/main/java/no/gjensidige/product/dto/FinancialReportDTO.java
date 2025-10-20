package no.gjensidige.product.dto;

import java.sql.Timestamp;

public class FinancialReportDTO {

    private long id;
    private Timestamp created;
    private ProductDTO highestMarginProduct;
    private ProductDTO lowestMarginProduct;
    private ProductDTO mostSoldProduct;
    private ProductDTO leastSoldProduct;
    private Double totalRevenue;
    private Double totalCost;
    private Double totalMargin;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public ProductDTO getHighestMarginProduct() {
        return highestMarginProduct;
    }

    public void setHighestMarginProduct(ProductDTO highestMarginProduct) {
        this.highestMarginProduct = highestMarginProduct;
    }

    public ProductDTO getLowestMarginProduct() {
        return lowestMarginProduct;
    }

    public void setLowestMarginProduct(ProductDTO lowestMarginProduct) {
        this.lowestMarginProduct = lowestMarginProduct;
    }

    public ProductDTO getMostSoldProduct() {
        return mostSoldProduct;
    }

    public void setMostSoldProduct(ProductDTO mostSoldProduct) {
        this.mostSoldProduct = mostSoldProduct;
    }

    public ProductDTO getLeastSoldProduct() {
        return leastSoldProduct;
    }

    public void setLeastSoldProduct(ProductDTO leastSoldProduct) {
        this.leastSoldProduct = leastSoldProduct;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getTotalMargin() {
        return totalMargin;
    }

    public void setTotalMargin(Double totalMargin) {
        this.totalMargin = totalMargin;
    }

}
