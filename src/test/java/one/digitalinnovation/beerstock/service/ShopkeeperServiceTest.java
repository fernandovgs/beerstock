package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.builder.ShopkeeperDTOBuilder;
import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;
import one.digitalinnovation.beerstock.domains.entities.Shopkeeper;
import one.digitalinnovation.beerstock.domains.mappers.ShopkeeperMapper;
import one.digitalinnovation.beerstock.domains.repositories.ShopkeeperRepository;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperAlreadyRegisteredException;
import one.digitalinnovation.beerstock.services.ShopkeeperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopkeeperServiceTest {

    private final ShopkeeperMapper shopkeeperMapper = ShopkeeperMapper.INSTANCE;

    @Mock
    private ShopkeeperRepository shopkeeperRepository;

    @InjectMocks
    private ShopkeeperService shopkeeperService;

    @Test
    void whenShopkeeperInformedThenItShouldBeCreated() throws ShopkeeperAlreadyRegisteredException {
        ShopkeeperDTO expectedShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedSavedShopkeeper = shopkeeperMapper.toModel(expectedShopkeeperDTO);

        when(shopkeeperRepository.findByName(expectedShopkeeperDTO.getName())).thenReturn(Optional.empty());
        when(shopkeeperRepository.save(expectedSavedShopkeeper)).thenReturn(expectedSavedShopkeeper);

        ShopkeeperDTO createdShopkeeperDTO = shopkeeperService.createShopkeeper(expectedShopkeeperDTO);

        assertThat(createdShopkeeperDTO.getId(), is(equalTo(expectedShopkeeperDTO.getId())));
        assertThat(createdShopkeeperDTO.getName(), is(equalTo(expectedShopkeeperDTO.getName())));
    }

    @Test
    void whenAlreadyRegisteredShopkeeperInformedThenThrowAnException() {
        ShopkeeperDTO expectedShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper duplicatedShopkeeper = shopkeeperMapper.toModel(expectedShopkeeperDTO);

        when(shopkeeperRepository.findByName(expectedShopkeeperDTO.getName()))
                .thenReturn(Optional.of(duplicatedShopkeeper));

        assertThrows(
                ShopkeeperAlreadyRegisteredException.class,
                () -> shopkeeperService.createShopkeeper(expectedShopkeeperDTO)
        );
    }
}
