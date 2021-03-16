package one.digitalinnovation.beerstock.builder;

import lombok.Builder;
import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.enums.BeerType;

/**
 * Builder is a creational pattern to make easier to generate object instances.
 * It is particularly useful when a given object can be instanced in several ways.
 * Source: https://refactoring.guru/pt-br/design-patterns/builder
 */

@Builder
public class BeerDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Brahma";

    @Builder.Default
    private final String brand = "Ambev";

    @Builder.Default
    private final int max = 50;

    @Builder.Default
    private final int quantity = 10;

    @Builder.Default
    private final BeerType type = BeerType.LAGER;

    public BeerDTO toBeerDTO() {
        return new BeerDTO(id,
                name,
                brand,
                max,
                quantity,
                type);
    }
}
