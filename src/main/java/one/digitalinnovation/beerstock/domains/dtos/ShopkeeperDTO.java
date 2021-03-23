package one.digitalinnovation.beerstock.domains.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopkeeperDTO {

    private Long id;

    @NotNull
    private String name;

    private List<BeerDTO> beers;
}
