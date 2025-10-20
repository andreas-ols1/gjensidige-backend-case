package no.gjensidige.product.entity;

import java.sql.Timestamp;

import jakarta.persistence.*;

/**
 * FinancialReport
 *
 * The model we would like to fill with data in exercise 2
 *
 */
@Entity
@Table(name = "financial_report")
public class FinancialReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Timestamp created;

    @ManyToOne
    @JoinColumn(name = "highest_margin_product_id")
    private Product highestMarginProduct;

    @ManyToOne
    @JoinColumn(name = "lowest_margin_product_id")
    private Product lowestMarginProduct;

    @ManyToOne
    @JoinColumn(name = "most_sold_product_id")
    private Product mostSoldProduct;

    @ManyToOne
    @JoinColumn(name = "least_sold_product_id")
    private Product leastSoldProduct;

    private Double totalRevenue;
    private Double totalCost;
    private Double totalMargin;

    @PrePersist
    private void onCreate() {
        this.created = new Timestamp(System.currentTimeMillis());
    }

    public Product getHighestMarginProduct() {
        return highestMarginProduct;
    }

    public void setHighestMarginProduct(Product highestMarginProduct) {
        this.highestMarginProduct = highestMarginProduct;
    }

    public Product getLowestMarginProduct() {
        return lowestMarginProduct;
    }

    public void setLowestMarginProduct(Product lowestMarginProduct) {
        this.lowestMarginProduct = lowestMarginProduct;
    }

    public Product getMostSoldProduct() {
        return mostSoldProduct;
    }

    public void setMostSoldProduct(Product mostSoldProduct) {
        this.mostSoldProduct = mostSoldProduct;
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

    public Timestamp getCreated() {
        return created;
    }

    public Product getLeastSoldProduct() {
        return leastSoldProduct;
    }

    public void setLeastSoldProduct(Product leastSoldProduct) {
        this.leastSoldProduct = leastSoldProduct;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

}
