package main.controllers;

import main.dto.EntryDTO;
import main.entities.Entry;
import main.entities.Invoice;
import main.entities.Item;
import main.repositories.EntryRepository;
import main.repositories.InvoiceRepository;
import main.repositories.ItemRepository;
import main.services.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static main.controllers.ItemCrudController.NO_ITEM_WITH_GIVEN_ID;

/**
 * Controller for CRUD operation upon on @Entry entity
 */
@RestController
@RequestMapping(path = "")
public class EntryCrudController {

    public static final String NO_INVOICE_WITH_GIVEN_ID = "No invoice with given id!";
    private EntryRepository entryRepository;

    private ItemRepository itemRepository;

    private InvoiceRepository invoiceRepository;

    private EntryService entryService;

    /**
     *
     * @param entryRepository
     * @param itemRepository
     * @param invoiceRepository
     * @param entryService
     */
    @Autowired
    public EntryCrudController(EntryRepository entryRepository, ItemRepository itemRepository, InvoiceRepository invoiceRepository, EntryService entryService) {
        this.entryRepository = entryRepository;
        this.itemRepository = itemRepository;
        this.invoiceRepository = invoiceRepository;
        this.entryService = entryService;
    }

    @GetMapping(value = "/get-all-entries")
    public ResponseEntity getAllEntries() {
        return ResponseEntity.ok(entryRepository.getAllEntries());
    }


    @DeleteMapping(value = "/remove-entry/{id}")
    public ResponseEntity removeEntry(@PathVariable("id") Integer id) {
        boolean isDeleted = entryRepository.removeEntry(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/get-entries-from-invoice")
    public ResponseEntity getAllEntriesFromInvoice(@RequestParam Integer invoiceId) {
        Invoice invoice = invoiceRepository.findInvoice(invoiceId);

        if (Objects.nonNull(invoice)) {
            List<EntryDTO> list = entryService.getEntriesByInvoiceIdAndSquash(invoice)
                    .stream()
                    .map(entry -> {
                        EntryDTO entryDTO = new EntryDTO();
                        entryDTO.setId(entry.getId());
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
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping(value = "/update-entry")
    public ResponseEntity<EntryDTO> updateEntry(@RequestBody EntryDTO entryDTO) {

        Item item = itemRepository.retrieveItem(entryDTO.getItemId());
        Invoice invoice = invoiceRepository.findInvoice(entryDTO.getInvoiceId());

        if (Objects.isNull(item)) {
            return formErrorMsg(entryDTO, NO_ITEM_WITH_GIVEN_ID);
        }

        if (Objects.isNull(invoice)) {
            return formErrorMsg(entryDTO, NO_INVOICE_WITH_GIVEN_ID);
        }

        entryRepository.updateEntry(entryDTO.getId(), item, entryDTO.getQty(), invoice, entryDTO.getSellPrice());
        return ResponseEntity.ok(entryDTO);
    }

    private ResponseEntity<EntryDTO> formErrorMsg(EntryDTO entryDTO, String msg) {
        entryDTO.setErrorMsg(msg);
        entryDTO.setInvoiceId(null);
        entryDTO.setId(null);
        entryDTO.setItemName(null);
        entryDTO.setSellPrice(null);
        entryDTO.setQty(null);
        return ResponseEntity.badRequest().body(entryDTO);
    }
}
