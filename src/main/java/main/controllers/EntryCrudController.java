package main.controllers;

import main.dto.EntryDTO;
import main.entities.Entry;
import main.entities.Invoice;
import main.entities.Item;
import main.repositories.EntryRepository;
import main.repositories.InvoiceRepository;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "")
public class EntryCrudController {

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

        if (Objects.nonNull(invoice)) {
            List<EntryDTO> list = entryRepository.getEntriesByInvoiceId(invoice)
                    .stream()
                    .map(objArray -> {
                        Entry entry = (Entry)objArray[0];
                        EntryDTO entryDTO = new EntryDTO();
                        entryDTO.setItemName(entry.getItem().getName());
                        entryDTO.setQty(entry.getQty());
                        entryDTO.setItemId(entry.getItem().getId());
                        entryDTO.setSellPrice(entry.getSellPrice());
                        return entryDTO;
                    }).collect(Collectors.toList());

            return ResponseEntity.ok(list);
        }
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

        body = entryRepository.addEntry(entryDTO.getId(), item, entryDTO.getQty(), invoice, entryDTO.getSellPrice());

        if (Objects.isNull(body)) {
            body = new Entry();
            body.setId(-1);
            body.setErrorMsg("Entry with id " + entryDTO.getId() + " is already exist!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        // Todo: change status in error case
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
