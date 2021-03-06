package main.controllers;

import main.entities.Client;
import main.repositories.ClientRepository;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(path = "")
public class ClientCrudController {
    public static final String NO_CLIENT_WITH_GIVEN_ID = "No client with given id!";

    private final ClientRepository clientRepository;

    public ClientCrudController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping(value = "/create-client")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client body = clientRepository.addClient(client.getId(), client.getName());
        if (Objects.isNull(body)) {
            body = new Client();
            body.setId(-1);
            body.setErrorMsg("Client with id " + client.getId() + " is already exist");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping(value = "/update-client")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        Client updatedClient = clientRepository.updateClient(client);
        if (Objects.isNull(updatedClient)) {
            client.setErrorMsg(NO_CLIENT_WITH_GIVEN_ID);
        }
        return ResponseEntity.ok(client);
    }

    @GetMapping(value = "/get-client/{id}")
    public ResponseEntity<Client> getClient(@PathVariable("id") Integer id) {
        Client body = clientRepository.retrieveClient(id);
        if (Objects.isNull(body)) {
            body = new Client();
            body.setId(-1);
            body.setErrorMsg(NO_CLIENT_WITH_GIVEN_ID);
        }
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "/get-all-clients")
    public ResponseEntity getAllClients() {
        return ResponseEntity.ok(clientRepository.getAllClients());
    }

    @DeleteMapping(value = "/remove-client/{id}")
    public ResponseEntity removeClient(@PathVariable("id") Integer id) {
        boolean isDeleted = clientRepository.removeClient(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


}