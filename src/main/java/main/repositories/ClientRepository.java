package main.repositories;

import main.entities.Client;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Transactional // will create a proxy to the ClientRepository instance, and inject
// transactional code on it
@Repository
public class ClientRepository {

    @PersistenceContext
    EntityManager entityManager;

    public synchronized Client addClient(int id, String name) {
        if (Objects.isNull(retrieveClient(id))) {
            Client client = new Client();
            client.setName(name);
            client.setId(id);
            entityManager.persist(client);
            return retrieveClient(id);
        } else {
            return null;
        }
    }

    public Client retrieveClient(Integer id) {
        return entityManager.find(Client.class, id);
    }

    public synchronized Client updateClient(Client client) {
        Client oldClient = retrieveClient(client.getId());
        if (Objects.nonNull(oldClient)) {
            oldClient.setName(client.getName());
            entityManager.flush();
            return oldClient;
        }
        return null;
    }

    public boolean removeClient(Integer clientId) {
        Client clientToRemove = retrieveClient(clientId);
        if (Objects.nonNull(clientToRemove)) {
            entityManager.remove(clientToRemove);
            return true;
        }
        return false;
    }

    public List<Client> getAllClients() {
        String jpql = "from Client as C ORDER BY C.name";
        return entityManager.createQuery(jpql).getResultList();
    }


}
