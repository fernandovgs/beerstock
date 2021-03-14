package one.digitalinnovation.beerstock.services;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.domains.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;
}
