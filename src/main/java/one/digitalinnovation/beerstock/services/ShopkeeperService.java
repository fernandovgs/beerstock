package one.digitalinnovation.beerstock.services;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;
import one.digitalinnovation.beerstock.domains.entities.Beer;
import one.digitalinnovation.beerstock.domains.entities.Shopkeeper;
import one.digitalinnovation.beerstock.domains.mappers.ShopkeeperMapper;
import one.digitalinnovation.beerstock.domains.repositories.ShopkeeperRepository;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerNotFoundException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperAlreadyRegisteredException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShopkeeperService {

    private final ShopkeeperRepository shopkeeperRepository;
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

    public List<ShopkeeperDTO> listShopkepeers() {
        return null;
    }

    public ShopkeeperDTO addBeersToShopkeeper(Long id, List<Long> beerIds) throws ShopkeeperNotFoundException {
        return null;
    }

    public ShopkeeperDTO removeBeersFromShopkeeper(Long id, List<Long> beerIds) throws ShopkeeperNotFoundException {
        return null;
    }

    private void verifyIfIsAlreadyRegistered(String name) throws ShopkeeperAlreadyRegisteredException {
        Optional<Shopkeeper> optionalShopkeeper = shopkeeperRepository.findByName(name);

        if (optionalShopkeeper.isPresent()) {
            throw new ShopkeeperAlreadyRegisteredException(name);
        }
    }
}
