package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.builder.BeerDTOBuilder;
import one.digitalinnovation.beerstock.builder.ShopkeeperDTOBuilder;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;
import one.digitalinnovation.beerstock.domains.entities.Beer;
import one.digitalinnovation.beerstock.domains.entities.Shopkeeper;
import one.digitalinnovation.beerstock.domains.mappers.BeerMapper;
import one.digitalinnovation.beerstock.domains.mappers.ShopkeeperMapper;
import one.digitalinnovation.beerstock.domains.repositories.ShopkeeperRepository;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerNotFoundException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.NoBeerProvidedException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperAlreadyRegisteredException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperNotFoundException;
import one.digitalinnovation.beerstock.services.BeerService;
import one.digitalinnovation.beerstock.services.ShopkeeperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopkeeperServiceTest {

    private final ShopkeeperMapper shopkeeperMapper = ShopkeeperMapper.INSTANCE;

    @Mock
    private ShopkeeperRepository shopkeeperRepository;

    @Mock
    private BeerService beerService;

    @InjectMocks
    private ShopkeeperService shopkeeperService;

    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

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

    @Test
    void whenExclusionIsCalledWithValidIdThenAShopkeeperShouldBeDeleted() throws ShopkeeperNotFoundException {
        ShopkeeperDTO expectedDeletedShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedDeletedShopkeeper = shopkeeperMapper.toModel(expectedDeletedShopkeeperDTO);

        when(shopkeeperRepository.findById(expectedDeletedShopkeeperDTO.getId()))
                .thenReturn(Optional.of(expectedDeletedShopkeeper));
        doNothing().when(shopkeeperRepository).deleteById(expectedDeletedShopkeeperDTO.getId());

        shopkeeperService.deleteById(expectedDeletedShopkeeperDTO.getId());

        verify(shopkeeperRepository, times(1))
                .findById(expectedDeletedShopkeeperDTO.getId());
        verify(shopkeeperRepository, times(1))
                .deleteById(expectedDeletedShopkeeperDTO.getId());
    }

    @Test
    void whenExclusionIsCalledWithInvalidIdThenAnExceptionShouldBeThrown() {
        ShopkeeperDTO expectedDeletedShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();

        when(shopkeeperRepository.findById(expectedDeletedShopkeeperDTO.getId())).thenReturn(Optional.empty());

        assertThrows(ShopkeeperNotFoundException.class, () -> shopkeeperService
                .deleteById(expectedDeletedShopkeeperDTO.getId()));
    }

    @Test
    void whenValidBeerIdAndValidShopkeeperInformedThenABeerShouldBeAddedToShopkeeperList()
            throws ShopkeeperNotFoundException, BeerNotFoundException, NoBeerProvidedException {
        // Given a shopkeeper and a beer that should be added to shopkeeper's list
        ShopkeeperDTO expectedShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedShopkeeper = shopkeeperMapper.toModel(expectedShopkeeperDTO);

        BeerDTO expectedAddedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedAddedBeer = beerMapper.toModel(expectedAddedBeerDTO);

        List<Long> expectedSingleBeerId = Collections.singletonList(1L);
        List<Beer> expectedAddedBeers = Collections.singletonList(expectedAddedBeer);
        List<BeerDTO> expectedAddedBeersDTO = Collections.singletonList(expectedAddedBeerDTO);

        when(shopkeeperRepository.findById(expectedShopkeeperDTO.getId()))
            .thenReturn(Optional.of(expectedShopkeeper));

        for (Long beerId: expectedSingleBeerId) {
            when(beerService.findById(beerId)).thenReturn(expectedAddedBeer);
        }

        expectedShopkeeper.setBeers(expectedAddedBeers);
        expectedShopkeeperDTO.setBeers(expectedAddedBeersDTO);

        when(shopkeeperRepository.save(expectedShopkeeper)).thenReturn(expectedShopkeeper);

        ShopkeeperDTO actualShopkeeperDTO = shopkeeperService
                .addBeersToShopkeeper(expectedShopkeeperDTO.getId(), expectedSingleBeerId);

        assertThat(actualShopkeeperDTO.getId(), is(equalTo(expectedShopkeeperDTO.getId())));
        assertThat(actualShopkeeperDTO.getBeers(), is(equalTo(expectedShopkeeperDTO.getBeers())));
    }

    @Test
    void whenValidBeersIdsAndValidShopkeeperInformedThenABeerShouldBeAddedToShopkeeperList()
            throws BeerNotFoundException, NoBeerProvidedException, ShopkeeperNotFoundException {
        // Given a shopkeeper and number of beers that should be added t shopkeeper's list
        ShopkeeperDTO expectedShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedShopkeeper = shopkeeperMapper.toModel(expectedShopkeeperDTO);

        List<Long> expectedBeerIDs = new ArrayList<>();
        Collections.addAll(expectedBeerIDs, 1L, 2L, 3L);
        List<BeerDTO> expectedAddedBeersDTOS = new ArrayList<>();
        List<Beer> expectedAddedBeers = new ArrayList<>();

        // adding beers' models and DTOs to it's respective lists
        for (Long beerId : expectedBeerIDs) {
            BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            expectedBeerDTO.setId(beerId);
            expectedBeerDTO.setName(expectedBeerDTO.getName() + beerId);

            Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

            // "when" conditions for each beer
            when(beerService.findById(beerId)).thenReturn(expectedBeer);

            expectedAddedBeersDTOS.add(expectedBeerDTO);
            expectedAddedBeers.add(expectedBeer);
        }

        when(shopkeeperRepository.findById(expectedShopkeeperDTO.getId()))
                .thenReturn(Optional.of(expectedShopkeeper));

        expectedShopkeeper.setBeers(expectedAddedBeers);
        expectedShopkeeperDTO.setBeers(expectedAddedBeersDTOS);

        when(shopkeeperRepository.save(expectedShopkeeper)).thenReturn(expectedShopkeeper);

        ShopkeeperDTO actualShopkeeperDTO = shopkeeperService
                .addBeersToShopkeeper(expectedShopkeeperDTO.getId(), expectedBeerIDs);

        assertThat(actualShopkeeperDTO.getId(), is(equalTo(expectedShopkeeperDTO.getId())));
        assertThat(actualShopkeeperDTO.getBeers(), is(equalTo(expectedShopkeeperDTO.getBeers())));
    }

    @Test
    void whenValidBeerIdAndInvalidShopkeeperInformedThenAnExceptionShouldBeThrownWhenAdding()
            throws BeerNotFoundException {
// Given a shopkeeper and a beer that should be added to shopkeeper's list
        ShopkeeperDTO expectedShopkeeperToFail = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();

        List<Long> expectedSingleBeerId = Collections.singletonList(1L);

        when(shopkeeperRepository.findById(expectedShopkeeperToFail.getId())).thenReturn(Optional.empty());

        assertThrows(ShopkeeperNotFoundException.class, () -> shopkeeperService
                .addBeersToShopkeeper(expectedShopkeeperToFail.getId(), expectedSingleBeerId));
    }

    @Test
    void whenNoBeersIdsAndValidShopkeeperInformedThenAnExceptionShouldBeThrownWhenAdding() {
        ShopkeeperDTO expectedShopkeeperToFail = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedShopkeeper = shopkeeperMapper.toModel(expectedShopkeeperToFail);

        List<Long> emptyBeerIdList = new ArrayList<>();

        when(shopkeeperRepository.findById(expectedShopkeeperToFail.getId()))
                .thenReturn(Optional.of(expectedShopkeeper));

        assertThrows(NoBeerProvidedException.class, () -> shopkeeperService
                .addBeersToShopkeeper(expectedShopkeeperToFail.getId(), emptyBeerIdList));
    }

    @Test
    void whenInvalidBeerIdAndValidShopkeeperInformedThenABeerShouldBeAddedToShopkeeperList()
            throws BeerNotFoundException {
        ShopkeeperDTO expectedShopkeeperToFail = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedShopkeeper = shopkeeperMapper.toModel(expectedShopkeeperToFail);

        List<Long> invalidBeerIdList = Collections.singletonList(7L);

        when(shopkeeperRepository.findById(expectedShopkeeperToFail.getId()))
                .thenReturn(Optional.of(expectedShopkeeper));
        when(beerService.findById(invalidBeerIdList.get(0))).thenThrow(BeerNotFoundException.class);

        assertThrows(BeerNotFoundException.class, () -> shopkeeperService
                .addBeersToShopkeeper(expectedShopkeeperToFail.getId(), invalidBeerIdList));
    }

    @Test
    void whenValidBeerIdAndValidShopkeeperInformedThenABeerShouldBeRemovedFromShopkeeperList()
            throws ShopkeeperNotFoundException, BeerNotFoundException, NoBeerProvidedException {
        // Given a shopkeeper and a beer that should be added to shopkeeper's list
        ShopkeeperDTO expectedShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedShopkeeper = shopkeeperMapper.toModel(expectedShopkeeperDTO);

        BeerDTO expectedRemovedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedRemovedBeer = beerMapper.toModel(expectedRemovedBeerDTO);

        List<Long> expectedSingleBeerId = Collections.singletonList(1L);

        when(shopkeeperRepository.findById(expectedShopkeeperDTO.getId()))
                .thenReturn(Optional.of(expectedShopkeeper));

        for (Long beerId: expectedSingleBeerId) {
            when(beerService.findById(beerId)).thenReturn(expectedRemovedBeer);
        }

        when(shopkeeperRepository.save(expectedShopkeeper)).thenReturn(expectedShopkeeper);

        ShopkeeperDTO actualShopkeeperDTO = shopkeeperService
                .removeBeersFromShopkeeper(expectedShopkeeperDTO.getId(), expectedSingleBeerId);

        assertThat(actualShopkeeperDTO.getId(), is(equalTo(expectedShopkeeperDTO.getId())));
        assertThat(actualShopkeeperDTO.getBeers(), is(empty()));
    }

    @Test
    void whenValidBeersIdsAndValidShopkeeperInformedThenABeerShouldBeRemovedFromShopkeeperList()
            throws BeerNotFoundException, NoBeerProvidedException, ShopkeeperNotFoundException {
        // Given a shopkeeper and number of beers that should be added t shopkeeper's list
        ShopkeeperDTO expectedShopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedShopkeeper = shopkeeperMapper.toModel(expectedShopkeeperDTO);

        List<Long> expectedBeerIDs = new ArrayList<>();
        Collections.addAll(expectedBeerIDs, 1L, 2L, 3L);

        // adding beers' models and DTOs to it's respective lists
        for (Long beerId : expectedBeerIDs) {
            BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
            expectedBeerDTO.setId(beerId);
            expectedBeerDTO.setName(expectedBeerDTO.getName() + beerId);

            Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

            // "when" conditions for each beer
            when(beerService.findById(beerId)).thenReturn(expectedBeer);
        }

        when(shopkeeperRepository.findById(expectedShopkeeperDTO.getId()))
                .thenReturn(Optional.of(expectedShopkeeper));


        when(shopkeeperRepository.save(expectedShopkeeper)).thenReturn(expectedShopkeeper);

        ShopkeeperDTO actualShopkeeperDTO = shopkeeperService
                .removeBeersFromShopkeeper(expectedShopkeeperDTO.getId(), expectedBeerIDs);

        assertThat(actualShopkeeperDTO.getId(), is(equalTo(expectedShopkeeperDTO.getId())));
        assertThat(actualShopkeeperDTO.getBeers(), is(empty()));
    }

    @Test
    void whenValidBeerIdAndInvalidShopkeeperInformedThenAnExceptionShouldBeThrownWhenRemoving()
            throws BeerNotFoundException {
// Given a shopkeeper and a beer that should be added to shopkeeper's list
        ShopkeeperDTO expectedShopkeeperToFail = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();

        List<Long> expectedSingleBeerId = Collections.singletonList(1L);

        when(shopkeeperRepository.findById(expectedShopkeeperToFail.getId())).thenReturn(Optional.empty());

        assertThrows(ShopkeeperNotFoundException.class, () -> shopkeeperService
                .removeBeersFromShopkeeper(expectedShopkeeperToFail.getId(), expectedSingleBeerId));
    }

    @Test
    void whenNoBeersIdsAndValidShopkeeperInformedThenAnExceptionShouldBeThrownWhenRemoving() {
        ShopkeeperDTO expectedShopkeeperToFail = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        Shopkeeper expectedShopkeeper = shopkeeperMapper.toModel(expectedShopkeeperToFail);

        List<Long> emptyBeerIdList = new ArrayList<>();

        when(shopkeeperRepository.findById(expectedShopkeeperToFail.getId()))
                .thenReturn(Optional.of(expectedShopkeeper));

        assertThrows(NoBeerProvidedException.class, () -> shopkeeperService
                .removeBeersFromShopkeeper(expectedShopkeeperToFail.getId(), emptyBeerIdList));
    }
}
