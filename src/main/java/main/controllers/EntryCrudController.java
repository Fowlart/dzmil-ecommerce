package main.controllers;

import main.dto.EntryDTO;
import main.entities.Invoice;
import main.entities.Entry;
import main.entities.Item;
import main.repositories.InvoiceRepository;
import main.repositories.EntryRepository;
import main.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(path = "")
public class EntryCrudController{

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping(value = "/get-all-entries")
    public ResponseEntity getAllEntries() {
        return ResponseEntity.ok(entryRepository.getAllEntries());
    }

    @GetMapping(value = "/get-entries-from-invoice")
    public ResponseEntity getAllEntriesFromInvoice(@RequestParam Integer invoiceId) {
        Invoice invoice = invoiceRepository.findInvoice(invoiceId);
        if (Objects.nonNull(invoice))  return ResponseEntity.ok(entryRepository.getEntriesByInvoiceId(invoice));
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/create-entry")
    public ResponseEntity<Entry> createEntry(@RequestBody EntryDTO entryDTO) {

        Entry body;

        Item item = itemRepository.retrieveItem(entryDTO.getItemId());

        if (Objects.isNull(item)) {
            body = new Entry();
            body.setId(-1);
            body.setErrorMsg("Item with id " + entryDTO.getItemId() + " is not exist!");
        }

        Invoice invoice = invoiceRepository.findInvoice(entryDTO.getInvoiceId());

        if (Objects.isNull(invoice)) {
            body = new Entry();
            body.setId(-1);
            body.setErrorMsg("Invoice with id " + entryDTO.getInvoiceId() + " is not exist!");
        }

        body = entryRepository.addEntry(entryDTO.getId(),item,entryDTO.getQty(), invoice,entryDTO.getSellPrice());

        if (Objects.isNull(body)) {
            body = new Entry();
            body.setId(-1);
            // Todo: ???
            body.setErrorMsg("Entry with id " + entryDTO.getId() + " is already exist!");
        }

        // Todo: change status in error case
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
