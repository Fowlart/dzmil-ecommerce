package main.controllers;

import main.dto.EntryDTO;
import main.entities.ClientInvoice;
import main.entities.Entry;
import main.entities.Item;
import main.repositories.ClientInvoiceRepository;
import main.repositories.EntryRepository;
import main.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private ClientInvoiceRepository clientInvoiceRepository;

    @GetMapping(value = "/get-all-entries")
    public ResponseEntity getAllEntries() {
        return ResponseEntity.ok(entryRepository.getAllEntries());
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

        ClientInvoice clientInvoice = clientInvoiceRepository.findInvoice(entryDTO.getClientInvoiceId());

        if (Objects.isNull(clientInvoice)) {
            body = new Entry();
            body.setId(-1);
            body.setErrorMsg("ClientInvoice with id " + entryDTO.getClientInvoiceId() + " is not exist!");
        }

        body = entryRepository.addEntry(entryDTO.getId(),item,entryDTO.getQty(),clientInvoice,entryDTO.getSellPrice());

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
