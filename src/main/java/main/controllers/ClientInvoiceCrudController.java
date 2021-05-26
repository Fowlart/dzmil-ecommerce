package main.controllers;

import main.entities.Client;
import main.entities.ClientInvoice;
import main.repositories.ClientInvoiceRepository;
import main.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(path = "")
public class ClientInvoiceCrudController {

    public static final String CLIENT_INVOICE_WITH_GIVEN_ID_ALREADY_EXIST = "ClientInvoice with given Id already exist!";
    public static final String INVOICE_ID_NOT_UNIQUE = "Given invoiceId not unique!";
    public static final String NO_CLIENT_INVOICE_WITH_GIVEN_ID = "No clientInvoice with given Id!";
    public static final String NO_CLIENT_WITH_GIVEN_ID = "No client with given id!";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientInvoiceRepository clientInvoiceRepository;

    @PostMapping(value = "/add-client-invoice")
    public ResponseEntity<ClientInvoice> addClientInvoice(@RequestBody ClientInvoice clientInvoice) {
        Client client = clientRepository.retrieveClient(clientInvoice.getClient().getId());
        Client supplier=null;
        if (Objects.nonNull(clientInvoice.getSupplier())) {
           supplier = clientRepository.retrieveClient(clientInvoice.getSupplier().getId());
        }

        // Client was not found
        if (Objects.isNull(client)) {
            return formErrorMsgInvoiceClientResponse(NO_CLIENT_WITH_GIVEN_ID, HttpStatus.NOT_FOUND);
        }

        // ClientInvoice with given Id already exist
        if (Objects.nonNull(clientInvoiceRepository.findInvoice(clientInvoice.getId()))) {
            return formErrorMsgInvoiceClientResponse(CLIENT_INVOICE_WITH_GIVEN_ID_ALREADY_EXIST, HttpStatus.CONFLICT);
        }

        try {
            clientInvoice = clientInvoiceRepository.addClientInvoice(clientInvoice.getId(), client,
                    clientInvoice.getInvoiceId(), clientInvoice.getInvoiceTotal(), supplier);
        } catch (Exception exception) {
            // not unique invoiceId
            return formErrorMsgInvoiceClientResponse(INVOICE_ID_NOT_UNIQUE, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(clientInvoice);
    }

    @GetMapping(value = "/get-client-invoice/{id}")
    public ResponseEntity<ClientInvoice> findInvoiceClient(@PathVariable Integer id) {
        ClientInvoice clientInvoice = clientInvoiceRepository.findInvoice(id);
        return Objects.nonNull(clientInvoice) ? ResponseEntity.ok(clientInvoice) :
                formErrorMsgInvoiceClientResponse(NO_CLIENT_INVOICE_WITH_GIVEN_ID, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ClientInvoice> formErrorMsgInvoiceClientResponse(String msg, HttpStatus status) {
        ClientInvoice err = new ClientInvoice();
        err.setInvoiceId(null);
        err.setClient(null);
        err.setDate(null);
        err.setInvoiceTotal(null);
        err.setId(null);
        err.setErrorMsg(msg);
        return ResponseEntity.status(status).body(err);
    }
}
