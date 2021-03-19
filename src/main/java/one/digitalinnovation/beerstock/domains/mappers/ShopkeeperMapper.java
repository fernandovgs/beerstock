package one.digitalinnovation.beerstock.domains.mappers;

import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;
import one.digitalinnovation.beerstock.domains.entities.Shopkeeper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper is a tool from mapstruct that automatically generates two common
 * methods, given the proper interface: toModel and toDTO. So we don't need
 * to add these methods in DTOs for every entity, saving a lot of time.
 */

@Mapper
public interface ShopkeeperMapper {
    ShopkeeperMapper INSTANCE = Mappers.getMapper(ShopkeeperMapper.class);

    Shopkeeper toModel(ShopkeeperDTO beerDTO);

    ShopkeeperDTO toDTO(Shopkeeper beer);
}
