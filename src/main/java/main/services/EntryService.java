package main.services;

import main.entities.Entry;
import main.entities.Invoice;
import main.repositories.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntryService {

    private final EntryRepository entryRepository;

    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public List<Entry> getEntriesByInvoiceIdAndSquash(Invoice invoice) {
        final List<Entry> foundEntries = entryRepository.getEntriesByInvoiceId(invoice).stream().map(arr -> (Entry) arr[0]).collect(Collectors.toList());

        List<Entry> filteredEntryForInvoice = new ArrayList<>();

        for (Entry entry : foundEntries) {

            if (filteredEntryForInvoice.stream().map(en -> en.getItem().getId()).noneMatch(id -> id.equals(entry.getItem().getId()))) {
                // filtered list of Entries do not has entry with such item

                Integer overallQtyOfEntryWithSameItem = foundEntries.stream().filter(entr -> entr.getItem().getId().equals(entry.getItem().getId())).map(Entry::getQty).reduce(Integer::sum).orElse(0);

                if (!overallQtyOfEntryWithSameItem.equals(entry.getQty())) {
                    entry.setQty(overallQtyOfEntryWithSameItem);
                }

                filteredEntryForInvoice.add(entry);
            }
            entryRepository.removeEntry(entry.getId());
        }
        if (filteredEntryForInvoice.size() != foundEntries.size()) {
            //rewrite squashed entry in DB
            filteredEntryForInvoice.forEach(entry -> entryRepository.addEntry(entry.getId(), entry.getItem(), entry.getQty(), entry.getInvoice(), entry.getSellPrice()));
        }
        return filteredEntryForInvoice;
    }
}
