package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.builder.BeerDTOBuilder;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.entities.Beer;
import one.digitalinnovation.beerstock.domains.mappers.BeerMapper;
import one.digitalinnovation.beerstock.domains.repositories.BeerRepository;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.services.BeerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

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
}
