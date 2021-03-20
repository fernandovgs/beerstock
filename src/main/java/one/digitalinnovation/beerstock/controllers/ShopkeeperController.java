package one.digitalinnovation.beerstock.controllers;

import one.digitalinnovation.beerstock.controllers.docs.ShopkeeperControllerDocs;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerNotFoundException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperAlreadyRegisteredException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static one.digitalinnovation.beerstock.constants.BeerstockConstants.*;

@RestController
@RequestMapping(BASE_URI_PATH + SHOPKEEPERS_URI_PATH)
public class ShopkeeperController implements ShopkeeperControllerDocs {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShopkeeperDTO createShopkeeper(@RequestBody @Valid ShopkeeperDTO shopkeeperDTO)
            throws ShopkeeperAlreadyRegisteredException {
        return null;
    }

    @GetMapping(NAME_URI_PATH)
    public ShopkeeperDTO findByName(@PathVariable String name)
            throws ShopkeeperNotFoundException {
        return null;
    }

    @GetMapping
    public List<ShopkeeperDTO> listShopkeepers() {
        return null;
    }

    @DeleteMapping(ID_URI_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(Long id) {

    }

    @PostMapping(ID_URI_PATH + ADD_BEER_TO_SHOPKEEPER_URI_PATH)
    public ShopkeeperDTO addBeersToShopkeeper(Long id, List<Long> beerIds)
            throws ShopkeeperNotFoundException, BeerNotFoundException {
        return null;
    }

    @PostMapping(ID_URI_PATH + RM_BEER_TO_SHOPKEEPER_URI_PATH)
    public ShopkeeperDTO removeBeersFromShopkeeper(Long id, List<Long> beerIds)
            throws ShopkeeperNotFoundException, BeerNotFoundException {
        return null;
    }
}
