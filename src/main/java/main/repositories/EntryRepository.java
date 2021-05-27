package main.repositories;

import main.entities.Entry;
import main.entities.Invoice;
import main.entities.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    public synchronized Entry addEntry(int id, Item item, Integer qty, Invoice invoice, BigDecimal sellPrice) {
        if (Objects.isNull(retrieveEntry(id))) {
            Entry entry = new Entry();
            entry.setId(id);
            entry.setItem(item);
            entry.setQty(qty);
            entry.setInvoice(invoice);
            entry.setSellPrice(sellPrice);
            entityManager.persist(entry);
            return retrieveEntry(id);
        } else {
            return null;
        }
    }

    public synchronized Entry updateEntry(int id, Item item, Integer qty, Invoice invoice, BigDecimal sellPrice) {
        Entry searchedEntry = retrieveEntry(id);
        if (Objects.nonNull(searchedEntry)) {
            searchedEntry.setItem(item);
            searchedEntry.setQty(qty);
            searchedEntry.setInvoice(invoice);
            searchedEntry.setSellPrice(sellPrice);
            entityManager.flush();
            return searchedEntry;
        }
        return null;
    }

    public List<Entry> getAllEntries() {
        String jpql = "from Entry as E ORDER BY E.id";
        return entityManager.createQuery(jpql).getResultList();
    }

    public List<Object[]> getEntriesByInvoiceId(Invoice invoice) {
        Query query = entityManager
                .createQuery("from Entry as e join e.invoice as i where i.id = " + invoice.getId());
        return query.getResultList();
    }

    public boolean removeEntry(Integer id) {
        Entry entryToRemove = retrieveEntry(id);
        if (Objects.nonNull(entryToRemove)) {
            entityManager.remove(entryToRemove);
            return true;
        }
        return false;
    }
}