package one.digitalinnovation.beerstock.services;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.entities.Beer;
import one.digitalinnovation.beerstock.domains.mappers.BeerMapper;
import one.digitalinnovation.beerstock.domains.repositories.BeerRepository;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(savedBeer);
    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer foundBeer = beerRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));
        return beerMapper.toDTO(foundBeer);
    }

    public List<BeerDTO> listAll() {
        return beerRepository.findAll().stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws BeerNotFoundException {
        verifyIfExists(id);
        beerRepository.deleteById(id);
    }

    /**
     * This method is private because there is no use outside here
     * @param name
     * @throws BeerAlreadyRegisteredException
     */
    private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<Beer> optionalSavedBeer = beerRepository.findByName(name);

        if (optionalSavedBeer.isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }

    /**
     * This method is private because there is no use outside here
     * @param name
     * @throws BeerNotFoundException
     */
    private Beer verifyIfExists(Long id) throws BeerNotFoundException{
        return beerRepository.findById(id).orElseThrow(() -> new BeerNotFoundException(id));
    }
}
