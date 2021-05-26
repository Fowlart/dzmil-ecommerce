package main.repositories;

import main.entities.ClientInvoice;
import main.entities.Entry;
import main.entities.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Transactional
@Repository
public class EntryRepository {

    @PersistenceContext
    EntityManager entityManager;

    public Entry retrieveEntry(Integer id) {
        return entityManager.find(Entry.class, id);
    }

    public synchronized Entry addEntry(int id, Item item, Integer qty, ClientInvoice clientInvoice, BigDecimal sellPrice) {
        if (Objects.isNull(retrieveEntry(id))) {
            Entry entry = new Entry();
            entry.setId(id);
            entry.setItem(item);
            entry.setQty(qty);
            entry.setClientInvoice(clientInvoice);
            entry.setSellPrice(sellPrice);
            entityManager.persist(entry);
            return retrieveEntry(id);
        } else {
            return null;
        }
    }

    public List<Entry> getAllEntries() {
        String jpql = "from Entry as E ORDER BY E.id";
        return entityManager.createQuery(jpql).getResultList();
    }
}