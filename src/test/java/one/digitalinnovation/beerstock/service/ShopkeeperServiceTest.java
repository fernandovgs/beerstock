package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.builder.BeerDTOBuilder;
import one.digitalinnovation.beerstock.builder.ShopkeeperDTOBuilder;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;
import one.digitalinnovation.beerstock.domains.entities.Beer;
import one.digitalinnovation.beerstock.domains.entities.Shopkeeper;
import one.digitalinnovation.beerstock.domains.mappers.ShopkeeperMapper;
import one.digitalinnovation.beerstock.domains.repositories.ShopkeeperRepository;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerNotFoundException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperAlreadyRegisteredException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperNotFoundException;
import one.digitalinnovation.beerstock.services.ShopkeeperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopkeeperServiceTest {

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

    @Test
    void whenValidShopkeeperNameIsGivenThenReturnAShopkeeper() throws ShopkeeperNotFoundException {
        // given
        ShopkeeperDTO expectedFoundShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedFoundShopkeeper = shopkeeperMapper.toModel(expectedFoundShopkeeperDTO);

        // when
        when(shopkeeperRepository.findByName((expectedFoundShopkeeperDTO.getName())))
                .thenReturn(Optional.of(expectedFoundShopkeeper));

        // then
        ShopkeeperDTO foundShopkeeperDTO = shopkeeperService.findByName(expectedFoundShopkeeperDTO.getName());

        assertThat(expectedFoundShopkeeperDTO, is(equalTo(foundShopkeeperDTO)));
    }


    @Test
    void whenUnregisteredShopkeeperInformedThenAnExceptionShouldBeThrown() {
        // given an informed beer
        ShopkeeperDTO expectedShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();

        // when
        when(shopkeeperRepository.findByName(expectedShopkeeperDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(
                ShopkeeperNotFoundException.class,
                () -> shopkeeperService.findByName(expectedShopkeeperDTO.getName())
        );
    }

    @Test
    void whenListShopkeeperIsCalledThenReturnListOfShopkeepers() {
        ShopkeeperDTO expectedFoundShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedFoundShopkeeper = shopkeeperMapper.toModel(expectedFoundShopkeeperDTO);

        when(shopkeeperRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundShopkeeper));
        List<ShopkeeperDTO> foundShopkeeperDTOS = shopkeeperService.listAll();

        assertThat(foundShopkeeperDTOS, is(not(empty())));
        assertThat(foundShopkeeperDTOS.get(0), is(equalTo(expectedFoundShopkeeperDTO)));
    }

    @Test
    void whenListShopkeeperIsCalledThenReturnEmptyList() {
        when(shopkeeperRepository.findAll()).thenReturn(Collections.emptyList());
        List<ShopkeeperDTO> foundShopkeeperDTOS = shopkeeperService.listAll();

        assertThat(foundShopkeeperDTOS, is(empty()));
    }
}
