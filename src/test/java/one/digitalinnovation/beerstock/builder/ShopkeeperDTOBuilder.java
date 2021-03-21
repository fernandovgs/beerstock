package one.digitalinnovation.beerstock.builder;

import lombok.Builder;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;

import java.util.Collections;
import java.util.List;

@Builder
public class ShopkeeperDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Armazem do suco de cevada";

    @Builder.Default
    private final List<BeerDTO> beers = Collections.emptyList();

    public ShopkeeperDTO toShopkeeperDTO() {
        return new ShopkeeperDTO(
                id,
                name,
                beers
        );
    }
}
