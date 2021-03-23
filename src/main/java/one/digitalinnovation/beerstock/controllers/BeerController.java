package one.digitalinnovation.beerstock.controllers;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.controllers.docs.BeerControllerDocs;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.dtos.QuantityDTO;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerNotFoundException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerStockExceededException;
import one.digitalinnovation.beerstock.services.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static one.digitalinnovation.beerstock.constants.BeerstockConstants.*;

@RestController
@RequestMapping(BASE_URI_PATH + BEERS_URI_PATH)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerController implements BeerControllerDocs {

    private final BeerService beerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDTO createBeer(@RequestBody @Valid BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        return beerService.createBeer(beerDTO);
    }

    @GetMapping(NAME_URI_PATH)
    public BeerDTO findByName(@PathVariable String name) throws BeerNotFoundException {
        return beerService.findByName(name);
    }

    @GetMapping
    public List<BeerDTO> listBeers() {
        return beerService.listAll();
    }

    @DeleteMapping(ID_URI_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws BeerNotFoundException {
        beerService.deleteById(id);
    }

    @PatchMapping(ID_URI_PATH + INCREMENT_URI_PATH)
    public BeerDTO increment(
            @PathVariable Long id,
            @RequestBody @Valid QuantityDTO quantityDTO
    ) throws BeerNotFoundException, BeerStockExceededException {
        return beerService.increment(id, quantityDTO.getQuantity());
    }
    @PatchMapping(ID_URI_PATH + DECREMENT_URI_PATH)
    public BeerDTO decrement(
            @PathVariable Long id,
            @RequestBody @Valid QuantityDTO quantityDTO
    ) throws BeerNotFoundException, BeerStockExceededException {
        return beerService.decrement(id, quantityDTO.getQuantity());
    }

}
