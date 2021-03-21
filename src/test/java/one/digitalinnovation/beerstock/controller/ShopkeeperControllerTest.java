package one.digitalinnovation.beerstock.controller;

import one.digitalinnovation.beerstock.builder.ShopkeeperDTOBuilder;
import one.digitalinnovation.beerstock.controllers.ShopkeeperController;
import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperNotFoundException;
import one.digitalinnovation.beerstock.services.ShopkeeperService;
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

import static one.digitalinnovation.beerstock.constants.BeerstockConstants.*;
import static one.digitalinnovation.beerstock.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShopkeeperControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShopkeeperService shopkeeperService;

    @InjectMocks
    private ShopkeeperController shopkeeperController;

    private static final Long VALID_SHOPKEEPER_ID = 1L;
    private static final Long INVALID_SHOPKEEPER_ID = 2L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shopkeeperController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAShopkeeperIsCreated() throws Exception {
        // given
        ShopkeeperDTO shopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();

        // when
        when(shopkeeperService.createShopkeeper(shopkeeperDTO)).thenReturn(shopkeeperDTO);

        // then
        mockMvc.perform(post(BASE_URI_PATH + SHOPKEEPERS_URI_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(shopkeeperDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(shopkeeperDTO.getName())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsCreated() throws Exception {
        // given
        ShopkeeperDTO shopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();
        shopkeeperDTO.setName(null);

        // then
        mockMvc.perform(post(BASE_URI_PATH + SHOPKEEPERS_URI_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(shopkeeperDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenAnOkStatusIsReturned() throws Exception {
        // given
        ShopkeeperDTO shopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();

        // when
        when(shopkeeperService.findByName(shopkeeperDTO.getName())).thenReturn(shopkeeperDTO);

        // then
        mockMvc.perform(get(BASE_URI_PATH + SHOPKEEPERS_URI_PATH + "/" + shopkeeperDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(shopkeeperDTO.getName())));
    }

    @Test
    void whenGETIsCalledWithoutValidNameThenAnNotFoundStatusIsReturned() throws Exception {
        // given
        ShopkeeperDTO shopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();

        // when
        when(shopkeeperService.findByName(shopkeeperDTO.getName())).thenThrow(ShopkeeperNotFoundException.class);

        // then
        mockMvc.perform(get(BASE_URI_PATH + SHOPKEEPERS_URI_PATH + "/" + shopkeeperDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        ShopkeeperDTO shopkeeperDTO = ShopkeeperDTOBuilder.builder().build().toShopkeeperDTO();

        // when
        when(shopkeeperService.listAll()).thenReturn(Collections.singletonList(shopkeeperDTO));

        // then
        mockMvc.perform(get(BASE_URI_PATH + SHOPKEEPERS_URI_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(shopkeeperDTO.getName())));
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(shopkeeperService).deleteById(VALID_SHOPKEEPER_ID);

        // then
        mockMvc.perform(delete(BASE_URI_PATH + SHOPKEEPERS_URI_PATH + "/" + VALID_SHOPKEEPER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doThrow(ShopkeeperNotFoundException.class).when(shopkeeperService).deleteById(INVALID_SHOPKEEPER_ID);

        // /api/v1/shopkeepers/2
        var path = BASE_URI_PATH + SHOPKEEPERS_URI_PATH + "/" + INVALID_SHOPKEEPER_ID;
        // then
        mockMvc.perform(delete(path)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
