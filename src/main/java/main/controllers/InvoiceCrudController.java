package main.controllers;

import main.entities.Client;
import main.entities.Invoice;
import main.repositories.ClientRepository;
import main.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "")
public class InvoiceCrudController {

    public static final String CLIENT_INVOICE_WITH_GIVEN_ID_ALREADY_EXIST = "Invoice with given Id already exist!";
    public static final String INVOICE_ID_NOT_UNIQUE = "Given invoiceId not unique!";
    public static final String NO_CLIENT_INVOICE_WITH_GIVEN_ID = "No clientInvoice with given Id!";
    public static final String NO_CLIENT_WITH_GIVEN_ID = "No client with given id!";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @PostMapping(value = "/add-invoice")
    public ResponseEntity<Invoice> addInvoice(@RequestBody Invoice invoice) {
        Client client = clientRepository.retrieveClient(invoice.getClient().getId());
        Client supplier = null;
        if (Objects.nonNull(invoice.getSupplier())) {
            supplier = clientRepository.retrieveClient(invoice.getSupplier().getId());
        }

        // Client was not found
        if (Objects.isNull(client)) {
            return formErrorMsgInvoiceClientResponse(NO_CLIENT_WITH_GIVEN_ID, HttpStatus.NOT_FOUND);
        }

        // Invoice with given Id already exist
        if (Objects.nonNull(invoiceRepository.findInvoice(invoice.getId()))) {
            return formErrorMsgInvoiceClientResponse(CLIENT_INVOICE_WITH_GIVEN_ID_ALREADY_EXIST, HttpStatus.CONFLICT);
        }

        try {
            invoice = invoiceRepository.addClientInvoice(invoice.getId(), client,
                    invoice.getInvoiceTotal(), supplier);
        } catch (Exception exception) {
            // not unique invoiceId
            return formErrorMsgInvoiceClientResponse(INVOICE_ID_NOT_UNIQUE, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(invoice);
    }

    @GetMapping(value = "/get-invoice/{id}")
    public ResponseEntity<Invoice> findInvoice(@PathVariable Integer id) {
        Invoice invoice = invoiceRepository.findInvoice(id);
        return Objects.nonNull(invoice) ? ResponseEntity.ok(invoice) :
                formErrorMsgInvoiceClientResponse(NO_CLIENT_INVOICE_WITH_GIVEN_ID, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/get-invoices")
    public ResponseEntity<List<Invoice>> findInvoiceByUserName(@RequestParam String userName) {
        List<Invoice> invoices = invoiceRepository.findInvoiceByCustomerOrSupplier(userName);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping(value = "/get-all-invoices")
    public ResponseEntity<List<Invoice>> findAllInvoices () {
        List<Invoice> invoices = invoiceRepository.getAllClientInvoices();
        return ResponseEntity.ok(invoices);
    }

    private ResponseEntity<Invoice> formErrorMsgInvoiceClientResponse(String msg, HttpStatus status) {
        Invoice err = new Invoice();
        err.setClient(null);
        err.setDate(null);
        err.setInvoiceTotal(null);
        err.setId(null);
        err.setErrorMsg(msg);
        return ResponseEntity.status(status).body(err);
    }
}
