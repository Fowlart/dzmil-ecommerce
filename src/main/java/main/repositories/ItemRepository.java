package main.repositories;


import main.entities.Client;
import main.entities.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Transactional
@Repository
public class ItemRepository {

    @PersistenceContext
    EntityManager entityManager;

    public Item retrieveItem(Integer id) {
        return entityManager.find(Item.class, id);
    }

    public synchronized Item addItem(int id, String name) {
        if (Objects.isNull(retrieveItem(id))) {
            Item item = new Item();
            item.setName(name);
            item.setId(id);
            entityManager.persist(item);
            return retrieveItem(id);
        } else {
            return null;
        }
    }

    public synchronized Item updateItem(Item item) {
        Item oldItem = retrieveItem(item.getId());
        if (Objects.nonNull(oldItem)) {
            oldItem.setName(item.getName());
            entityManager.flush();
            return oldItem;
        }
        return null;
    }

    public boolean removeItem(Integer itemId) {
        Item itemToRemove = retrieveItem(itemId);
        if (Objects.nonNull(itemToRemove)) {
            entityManager.remove(itemToRemove);
            return true;
        }
        return false;
    }

    public List<Item> getAllItems() {
        String jpql = "from Item as I ORDER BY I.name";
        return entityManager.createQuery(jpql).getResultList();
    }


}
