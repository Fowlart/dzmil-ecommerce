package main.repositories;


import main.entities.Client;
import main.entities.Invoice;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Repository
public class InvoiceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Invoice addClientInvoice(Integer id, Client client, BigDecimal invoiceTotal, Client supplier) {
        Invoice invoice = new Invoice();
        invoice.setSupplier(supplier);
        invoice.setId(id);
        invoice.setDate(LocalDate.now());
        invoice.setClient(client);
        invoice.setInvoiceTotal(invoiceTotal);
        entityManager.persist(invoice);
        return invoice;
    }

    public Invoice findInvoice(Integer id) {
        return entityManager.find(Invoice.class, id);
    }

    public List<Invoice> getAllClientInvoices() {
        // Todo: need to optimize sql query
        String jpql = "from Invoice as C ORDER BY C.date DESC";
        return entityManager.createQuery(jpql).getResultList();
    }

    public List<Invoice> findInvoiceByCustomerOrSupplier(String clientName) {
        return getAllClientInvoices().stream().filter(clientInvoice -> clientInvoice.getClient().getName().equals(clientName) ||
                Objects.nonNull(clientInvoice.getSupplier()) && clientInvoice.getSupplier().getName().equals(clientName)).collect(Collectors.toList());
    }
}
