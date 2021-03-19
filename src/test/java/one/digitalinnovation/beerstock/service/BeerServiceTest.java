package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.builder.BeerDTOBuilder;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.entities.Beer;
import one.digitalinnovation.beerstock.domains.mappers.BeerMapper;
import one.digitalinnovation.beerstock.domains.repositories.BeerRepository;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerNotFoundException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerStockExceededException;
import one.digitalinnovation.beerstock.services.BeerService;
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
class BeerServiceTest {

    private static final long INVALID_BEER_ID = 1L;

    @Mock
    private BeerRepository beerRepository;

    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    @InjectMocks
    private BeerService beerService;

    @Test
    void whenBeerInformedThenItShouldBeCreated() throws BeerAlreadyRegisteredException {
        // given an informed beer
        BeerDTO expectedDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedSavedBeer = beerMapper.toModel(expectedDTO);

        // when
        // When static method from Mockito is called, we are saying that when a given method is
        // called, then Mockito returns a given result.
        // Checking two 'when' method calls below:
        //  1. when method findByName from BeerRepository interface is called, then Mockito returns
        //      an empty optional object.
        //  2. when method save from BeerRepository is called, then it must return the exact object
        //      that was passed via parameter on 'save'.
        when(beerRepository.findByName(expectedDTO.getName())).thenReturn(Optional.empty());
        when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);

        // then
        BeerDTO createdBeerDTO = beerService.createBeer(expectedDTO);

        // Assert using Hamcrest
        // IMPORTANT: assertThat(ACTUAL, EXPECTED)
        assertThat(createdBeerDTO.getId(), is(equalTo(expectedDTO.getId())));
        assertThat(createdBeerDTO.getName(), is(equalTo(expectedDTO.getName())));
        assertThat(createdBeerDTO.getQuantity(), is(equalTo(expectedDTO.getQuantity())));

        // Assert using 'classic' junit assertions
        // IMPORTANT: assertEquals(EXPECTED, ACTUAL)
        assertEquals(expectedDTO.getId(), createdBeerDTO.getId());
        assertEquals(expectedDTO.getName(), createdBeerDTO.getName());
        assertEquals(expectedDTO.getQuantity(), createdBeerDTO.getQuantity());

        // Other Hamcrest assertions
        assertThat(createdBeerDTO.getQuantity(), is(greaterThan(2)));
    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
        // given an informed beer
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer duplicatedBeer = beerMapper.toModel(expectedBeerDTO);

        // when
        when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.of(duplicatedBeer));

        // then
        assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(expectedBeerDTO));
    }

    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
        // given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

        // when
        when(beerRepository.findByName((expectedFoundBeerDTO.getName()))).thenReturn(Optional.of(expectedFoundBeer));

        // then
        BeerDTO foundBeerDTO = beerService.findByName(expectedFoundBeerDTO.getName());

        assertThat(expectedFoundBeerDTO, is(equalTo(foundBeerDTO)));
    }

    @Test
    void whenUnregisteredBeerInformedThenAnExceptionShouldBeThrown() {
        // given an informed beer
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // when
        when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(BeerNotFoundException.class, () -> beerService.findByName(expectedBeerDTO.getName()));
    }

    @Test
    void whenListBeerIsCalledThenReturnListOfBeers() {
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

        when(beerRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundBeer));
        List<BeerDTO> foundBeerDTOS = beerService.listAll();

        assertThat(foundBeerDTOS, is(not(empty())));
        assertThat(foundBeerDTOS.get(0), is(equalTo(expectedFoundBeerDTO)));
    }

    @Test
    void whenListBeerIsCalledThenReturnEmptyList() {
        when(beerRepository.findAll()).thenReturn(Collections.emptyList());
        List<BeerDTO> foundBeerDTOS = beerService.listAll();

        assertThat(foundBeerDTOS, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundException {
        BeerDTO expectedDeletedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedDeletedBeer = beerMapper.toModel(expectedDeletedBeerDTO);

        when(beerRepository.findById(expectedDeletedBeerDTO.getId())).thenReturn(Optional.of(expectedDeletedBeer));
        doNothing().when(beerRepository).deleteById(expectedDeletedBeerDTO.getId());

        beerService.deleteById(expectedDeletedBeerDTO.getId());

        verify(beerRepository, times(1)).findById(expectedDeletedBeerDTO.getId());
        verify(beerRepository, times(1)).deleteById(expectedDeletedBeerDTO.getId());
    }

    @Test
    void whenExclusionIsCalledWithInvalidIdThenABeerShouldBeDeleted() {
        BeerDTO expectedDeletedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        when(beerRepository.findById(expectedDeletedBeerDTO.getId())).thenReturn(Optional.empty());
        assertThrows(BeerNotFoundException.class, () -> beerService.deleteById(expectedDeletedBeerDTO.getId()));
    }

    @Test
    void whenIncrementIsCalledThenIncrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel((expectedBeerDTO));

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedBeerDTO.getQuantity() + quantityToIncrement;

        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
        when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);

        BeerDTO incrementedBeerDTO = beerService.increment(expectedBeerDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedBeerDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDTO.getMax()));
    }

    @Test
    void whenIncrementValueIsGreaterThanAllowedThenThrowException() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));

        int invalidQuantity = 41; // edge case: 10 + 41 > 50
        assertThrows(BeerStockExceededException.class, () -> beerService.increment(INVALID_BEER_ID, invalidQuantity));
    }

    @Test
    void whenDecrementIsCalledThenIncrementBeerStock() throws Exception {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel((expectedBeerDTO));

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement;
        expectedQuantityAfterIncrement = expectedBeerDTO.getQuantity() - quantityToIncrement;

        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
        when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);

        BeerDTO incrementedBeerDTO = beerService.decrement(expectedBeerDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedBeerDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDTO.getMax()));
    }

    @Test
    void whenDecrementValueIsGreaterThanAllowedThenThrowException() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));

        int invalidQuantity = 11; // edge case: 10 - 11 < 0
        assertThrows(BeerStockExceededException.class, () -> beerService.decrement(INVALID_BEER_ID, invalidQuantity));
    }
}
