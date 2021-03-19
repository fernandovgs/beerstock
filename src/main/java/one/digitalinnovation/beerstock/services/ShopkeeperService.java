package one.digitalinnovation.beerstock.services;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;
import one.digitalinnovation.beerstock.domains.mappers.ShopkeeperMapper;
import one.digitalinnovation.beerstock.domains.repositories.ShopkeeperRepository;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperAlreadyRegisteredException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShopkeeperService {

    private final ShopkeeperRepository shopkeeperRepository;
    private static final ShopkeeperMapper shopkeeperMapper = ShopkeeperMapper.INSTANCE;

    public ShopkeeperDTO createShopkeeper(ShopkeeperDTO shopkeeperDTO) throws ShopkeeperAlreadyRegisteredException {
        return null;
    }

    public ShopkeeperDTO findByName(String name) throws ShopkeeperNotFoundException {
        return null;
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
}
