package main.repositories;


import main.entities.Client;
import main.entities.ClientInvoice;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

@Transactional
@Repository
public class ClientInvoiceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ClientInvoice addClientInvoice(Integer id, Client client, Integer invoiceId, BigDecimal invoiceTotal) {
        ClientInvoice clientInvoice = new ClientInvoice();
        clientInvoice.setId(id);
        clientInvoice.setDate(LocalDate.now());
        clientInvoice.setClient(client);
        clientInvoice.setInvoiceId(invoiceId);
        clientInvoice.setInvoiceTotal(invoiceTotal);
        entityManager.persist(clientInvoice);
        return clientInvoice;
    }

    public ClientInvoice findInvoice(Integer id) {
        return entityManager.find(ClientInvoice.class,id);
    }
}
