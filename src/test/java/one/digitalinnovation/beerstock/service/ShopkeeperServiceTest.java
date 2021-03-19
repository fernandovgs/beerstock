package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.domains.mappers.ShopkeeperMapper;
import one.digitalinnovation.beerstock.domains.repositories.ShopkeeperRepository;
import one.digitalinnovation.beerstock.services.ShopkeeperService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShopkeeperServiceTest {

    private final ShopkeeperMapper shopkeeperMapper = ShopkeeperMapper.INSTANCE;

    @Mock
    private ShopkeeperRepository shopkeeperRepository;

    @InjectMocks
    private ShopkeeperService shopkeeperService;

}
