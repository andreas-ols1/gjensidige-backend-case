package no.gjensidige.product.controller.advice;

import no.gjensidige.product.exception.ReportNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Response controller for ReportNotFoundException
 *
 */
@ControllerAdvice
public class ReportNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ReportNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String productNotFoundAdvice(ReportNotFoundException ex) {
        return ex.getMessage();
    }

}
