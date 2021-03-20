package one.digitalinnovation.beerstock.services;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;
import one.digitalinnovation.beerstock.domains.entities.Beer;
import one.digitalinnovation.beerstock.domains.entities.Shopkeeper;
import one.digitalinnovation.beerstock.domains.mappers.ShopkeeperMapper;
import one.digitalinnovation.beerstock.domains.repositories.ShopkeeperRepository;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerNotFoundException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.NoBeerProvidedException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperAlreadyRegisteredException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShopkeeperService {

    private final ShopkeeperRepository shopkeeperRepository;

    private final BeerService beerService;

    private static final ShopkeeperMapper shopkeeperMapper = ShopkeeperMapper.INSTANCE;


    public ShopkeeperDTO createShopkeeper(ShopkeeperDTO shopkeeperDTO)
            throws ShopkeeperAlreadyRegisteredException {

        verifyIfIsAlreadyRegistered(shopkeeperDTO.getName());

        Shopkeeper shopkeeper = shopkeeperMapper.toModel(shopkeeperDTO);
        Shopkeeper savedShopkeeper = shopkeeperRepository.save(shopkeeper);

        return shopkeeperMapper.toDTO(savedShopkeeper);
    }

    public ShopkeeperDTO findByName(String name) throws ShopkeeperNotFoundException {
        Shopkeeper foundShopkeeper = shopkeeperRepository.findByName(name)
                .orElseThrow(() -> new ShopkeeperNotFoundException(name));
        return shopkeeperMapper.toDTO(foundShopkeeper);
    }

    public List<ShopkeeperDTO> listAll() {
        return shopkeeperRepository.findAll().stream()
                .map(shopkeeperMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws ShopkeeperNotFoundException {
        verifyIfExists(id);
        shopkeeperRepository.deleteById(id);
    }

    public ShopkeeperDTO addBeersToShopkeeper(Long id, List<Long> beerIds)
            throws ShopkeeperNotFoundException, BeerNotFoundException, NoBeerProvidedException {
        Shopkeeper shopkeeper = verifyIfExists(id);
        List<Beer> beers = new ArrayList<>();

        if (beerIds == null || beerIds.isEmpty()) {
            throw new NoBeerProvidedException();
        }

        for (Long beerId: beerIds) {
            Beer beer = beerService.findById(beerId);
            beers.add(beer);
        }
        shopkeeper.setBeers(beers);
        Shopkeeper savedShopkeeper = shopkeeperRepository.save(shopkeeper);

        return shopkeeperMapper.toDTO(savedShopkeeper);
    }

    public ShopkeeperDTO removeBeersFromShopkeeper(Long id, List<Long> beerIds)
            throws ShopkeeperNotFoundException, BeerNotFoundException, NoBeerProvidedException {
        Shopkeeper shopkeeper = verifyIfExists(id);
        List<Beer> beers = new ArrayList<>();

        if (beerIds == null || beerIds.isEmpty()) {
            throw new NoBeerProvidedException();
        }

        for (Long beerId: beerIds) {
            Beer beer = beerService.findById(beerId);
            beers.add(beer);
        }

        shopkeeper.setBeers(
                shopkeeper.getBeers()
                        .stream()
                        .filter(e -> !beerIds.contains(e.getId()))
                        .collect(Collectors.toList()));

        Shopkeeper savedShopkeeper = shopkeeperRepository.save(shopkeeper);

        return shopkeeperMapper.toDTO(savedShopkeeper);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws ShopkeeperAlreadyRegisteredException {
        Optional<Shopkeeper> optionalShopkeeper = shopkeeperRepository.findByName(name);

        if (optionalShopkeeper.isPresent()) {
            throw new ShopkeeperAlreadyRegisteredException(name);
        }
    }

    private Shopkeeper verifyIfExists(Long id) throws ShopkeeperNotFoundException{
        return shopkeeperRepository.findById(id).orElseThrow(() -> new ShopkeeperNotFoundException(id));
    }
}
