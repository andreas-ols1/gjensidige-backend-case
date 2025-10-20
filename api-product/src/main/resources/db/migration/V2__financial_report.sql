CREATE TABLE financial_report (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    total_revenue numeric,
    total_cost numeric,
    total_margin numeric,
    most_sold_product_id INTEGER,
    least_sold_product_id INTEGER,
    highest_margin_product_id INTEGER,
    lowest_margin_product_id INTEGER,
    FOREIGN KEY (most_sold_product_id) REFERENCES product(id),
    FOREIGN KEY (least_sold_product_id) REFERENCES product(id),
    FOREIGN KEY (highest_margin_product_id) REFERENCES product(id),
    FOREIGN KEY (lowest_margin_product_id) REFERENCES product(id)
)