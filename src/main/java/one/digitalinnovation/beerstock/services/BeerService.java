package one.digitalinnovation.beerstock.services;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.entities.Beer;
import one.digitalinnovation.beerstock.domains.mappers.BeerMapper;
import one.digitalinnovation.beerstock.domains.repositories.BeerRepository;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerNotFoundException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerStockExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;
    private static final BeerMapper beerMapper = BeerMapper.INSTANCE;

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
        findById(id);
        beerRepository.deleteById(id);
    }

    public BeerDTO increment(Long id, int quantityToIncrement)
            throws BeerNotFoundException, BeerStockExceededException {
        Beer beerToIncrement = findById(id);

        if (beerToIncrement.getQuantity() + quantityToIncrement <= beerToIncrement.getMax()) {
            beerToIncrement.setQuantity(beerToIncrement.getQuantity() + quantityToIncrement);
            return beerMapper.toDTO(beerRepository.save(beerToIncrement));
        }

        throw new BeerStockExceededException(id, quantityToIncrement);
    }

    public BeerDTO decrement(Long id, int quantityToDecrement)
            throws BeerNotFoundException, BeerStockExceededException {
        Beer beerToDecrement = findById(id);

        if (beerToDecrement.getQuantity() - quantityToDecrement >= 0) {
            beerToDecrement.setQuantity(beerToDecrement.getQuantity() - quantityToDecrement);
            return beerMapper.toDTO(beerRepository.save(beerToDecrement));
        }

        throw new BeerStockExceededException(id, quantityToDecrement);
    }

    /**
     * This method is private because there is no use outside here
     * @param name beer name
     * @throws BeerAlreadyRegisteredException when beer has been registered already
     */
    private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<Beer> optionalSavedBeer = beerRepository.findByName(name);

        if (optionalSavedBeer.isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }

    /**
     * This method was turned to public because there use outside this class.
     * Also, it's name was changed from "verifyIfExists" to findById.
     *
     * @param id                        id to search
     * @throws BeerNotFoundException    in case of no beer is found, given id
     */
    public Beer findById(Long id) throws BeerNotFoundException{
        return beerRepository.findById(id).orElseThrow(() -> new BeerNotFoundException(id));
    }
}
