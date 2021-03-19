package one.digitalinnovation.beerstock.controllers.docs;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import one.digitalinnovation.beerstock.domains.dtos.ShopkeeperDTO;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperNotFoundException;
import one.digitalinnovation.beerstock.infrastructure.exceptions.ShopkeeperAlreadyRegisteredException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages beer's available shopkeepers")
public interface ShopkeeperControllerDocs {

    @ApiOperation(value = "Create shopkeeper operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success shopkeeper creation")
    })
    ShopkeeperDTO createShopkeeper(ShopkeeperDTO shopkeeperDTO) throws ShopkeeperAlreadyRegisteredException;

    @ApiOperation(value = "Returns shopkeeper found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success shopkeeper found in the system"),
            @ApiResponse(code = 404, message = "Shopkeeper with given name not found.")
    })
    ShopkeeperDTO findByName(@PathVariable String name) throws ShopkeeperNotFoundException;

    @ApiOperation(value = "Returns a list of all shopkeepers registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all shopkeepers registered in the system"),
    })
    List<ShopkeeperDTO> listShopkeepers();
}
