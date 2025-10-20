package no.gjensidige.product.exception;

/**
 *
 * Exception thrown when product is not in the database.
 *
 */
public class ReportNotFoundException extends RuntimeException {

    public ReportNotFoundException(Long id) {
        super("Could not find report with id : " + id);
    }
}
