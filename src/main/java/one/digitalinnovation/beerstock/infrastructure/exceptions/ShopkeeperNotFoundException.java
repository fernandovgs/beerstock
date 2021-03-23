package one.digitalinnovation.beerstock.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShopkeeperNotFoundException extends Exception {

    public ShopkeeperNotFoundException(String beerName) {
        super(String.format("Shopkeeper with name %s not found in the system.", beerName));
    }

    public ShopkeeperNotFoundException(Long id) {
        super(String.format("Shopkeeper with id %s not found in the system.", id));
    }
}

