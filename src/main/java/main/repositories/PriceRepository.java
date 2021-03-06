package main.repositories;

import main.entities.Item;
import main.entities.Price;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Transactional
@Repository
public class PriceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final ItemRepository itemRepository;

    public PriceRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Price addPrice(Integer id, Item item, BigDecimal price, LocalDate date) {
        Price newPrice = new Price();
        newPrice.setId(id);
        newPrice.setDate(date);
        newPrice.setItem(item);
        newPrice.setPrice(price);
        entityManager.persist(newPrice);
        return newPrice;
    }

    public Price findPrice(Integer id) {
        return entityManager.find(Price.class, id);
    }


    public List<Price> getAllPrices() {
        String jpql = "from Price as P ORDER BY P.date";
        return entityManager.createQuery(jpql).getResultList();
    }

    public boolean removePrice(Integer priceId) {
        Price priceToRemove = findPrice(priceId);
        if (Objects.nonNull(priceToRemove)) {
            entityManager.remove(priceToRemove);
            return true;
        }
        return false;
    }




}
