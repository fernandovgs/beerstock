package one.digitalinnovation.beerstock.controller;

import one.digitalinnovation.beerstock.builder.BeerDTOBuilder;
import one.digitalinnovation.beerstock.controllers.BeerController;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.dtos.QuantityDTO;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerNotFoundException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.BeerStockExceededException;
import one.digitalinnovation.beerstock.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static one.digitalinnovation.beerstock.utils.JsonConversionUtils.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
import static one.digitalinnovation.beerstock.constants.BeerstockConstants.*;

@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

    private static final long VALID_BEER_ID = 1L;
    private static final long INVALID_BEER_ID = 2L;

    // To mock endpoint requests (can mock GraphQL, which is a "simple" POST at the end of the day)
    private MockMvc mockMvc;

    @Mock // What we WANT to mock
    private BeerService beerService;

    @InjectMocks // What we WANT TO INJECT mocked classes indicated by @Mock
    private BeerController beerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(beerController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
            .build();
    }

    @Test
    void whenPOSTIsCalledThenABeerIsCreated() throws Exception {
        // given
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // when
        when(beerService.createBeer(beerDTO)).thenReturn(beerDTO);

        // then
        mockMvc.perform(post(BASE_URI_PATH + BEERS_URI_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(beerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.beerType", is(beerDTO.getBeerType().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsCreated() throws Exception {
        // given
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setBrand(null);

        // then
        mockMvc.perform(post(BASE_URI_PATH + BEERS_URI_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(beerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenAnOkStatusIsReturned() throws Exception {
        // given
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // when
        when(beerService.findByName(beerDTO.getName())).thenReturn(beerDTO);

        // then
        mockMvc.perform(get(BASE_URI_PATH + BEERS_URI_PATH + "/" + beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.beerType", is(beerDTO.getBeerType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutValidNameThenAnNotFoundStatusIsReturned() throws Exception {
        // given
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // when
        when(beerService.findByName(beerDTO.getName())).thenThrow(BeerNotFoundException.class);

        // then
        mockMvc.perform(get(BASE_URI_PATH + BEERS_URI_PATH + "/" + beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // when
        when(beerService.listAll()).thenReturn(Collections.singletonList(beerDTO));

        // then
        mockMvc.perform(get(BASE_URI_PATH + BEERS_URI_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(beerDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$[0].beerType", is(beerDTO.getBeerType().toString())));
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(beerService).deleteById(VALID_BEER_ID);

        // then
        mockMvc.perform(delete(BASE_URI_PATH + BEERS_URI_PATH + "/" + VALID_BEER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(BeerNotFoundException.class).when(beerService).deleteById(INVALID_BEER_ID);

        // /api/v1/beers/2
        var path = BASE_URI_PATH + BEERS_URI_PATH + "/" + INVALID_BEER_ID;
        // then
        mockMvc.perform(delete(path)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementBeerThenOkStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder().quantity(40).build(); // edge case: 40 + 10 <= 50
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());

        when(beerService.increment(VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(beerDTO);

        var urlPath = BASE_URI_PATH + BEERS_URI_PATH + "/" + VALID_BEER_ID + INCREMENT_URI_PATH;
        mockMvc.perform(patch(urlPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.beerType", is(beerDTO.getBeerType().toString())))
                .andExpect(jsonPath("$.quantity", is(beerDTO.getQuantity())));
    }

    @Test
    void whenPATCHIsCalledToInvalidIncrementBeerThenBadRequestStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder().quantity(41).build(); // edge case: 41 + 10 <= 50
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());

        doThrow(BeerStockExceededException.class)
                .when(beerService)
                .increment(VALID_BEER_ID, quantityDTO.getQuantity());

        var urlPath = BASE_URI_PATH + BEERS_URI_PATH + "/" + VALID_BEER_ID + INCREMENT_URI_PATH;
        mockMvc.perform(patch(urlPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPATCHIsCalledToDecrementBeerThenOkStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder().quantity(10).build(); // edge case: 10 - 10 >= 0
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        beerDTO.setQuantity(beerDTO.getQuantity() - quantityDTO.getQuantity());

        when(beerService.decrement(VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(beerDTO);

        var urlPath = BASE_URI_PATH + BEERS_URI_PATH + "/" + VALID_BEER_ID + DECREMENT_URI_PATH;
        mockMvc.perform(patch(urlPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.beerType", is(beerDTO.getBeerType().toString())))
                .andExpect(jsonPath("$.quantity", is(beerDTO.getQuantity())));
    }

    @Test
    void whenPATCHIsCalledToInvalidDecrementBeerThenBadRequestStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder().quantity(11).build(); // edge case: 10 - 11 < 0
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        beerDTO.setQuantity(beerDTO.getQuantity() - quantityDTO.getQuantity());

        doThrow(BeerStockExceededException.class)
                .when(beerService)
                .decrement(VALID_BEER_ID, quantityDTO.getQuantity());

        var urlPath = BASE_URI_PATH + BEERS_URI_PATH + "/" + VALID_BEER_ID + "/" + DECREMENT_URI_PATH;
        mockMvc.perform(patch(urlPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isBadRequest());
    }
}
