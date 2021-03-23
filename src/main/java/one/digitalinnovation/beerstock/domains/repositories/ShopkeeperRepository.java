package one.digitalinnovation.beerstock.domains.repositories;

import one.digitalinnovation.beerstock.domains.entities.Shopkeeper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopkeeperRepository extends JpaRepository<Shopkeeper, Long> {
    Optional<Shopkeeper> findByName(String name);
}
