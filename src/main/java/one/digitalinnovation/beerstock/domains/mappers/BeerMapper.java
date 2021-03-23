package one.digitalinnovation.beerstock.domains.mappers;

import one.digitalinnovation.beerstock.domains.dtos.BeerDTO;
import one.digitalinnovation.beerstock.domains.entities.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper is a tool from mapstruct that automatically generates two common
 * methods, given the proper interface: toModel and toDTO. So we don't need
 * to add these methods in DTOs for every entity, saving a lot of time.
 */

@Mapper
public interface BeerMapper {
    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    Beer toModel(BeerDTO beerDTO);

    BeerDTO toDTO(Beer beer);
}
