package one.digitalinnovation.beerstock.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoBeerProvidedException extends Exception {

    public NoBeerProvidedException() {
        super("No beer was provided.");
    }
}