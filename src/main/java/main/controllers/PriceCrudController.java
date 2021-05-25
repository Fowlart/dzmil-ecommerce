package main.controllers;

import main.dto.PriceDTO;
import main.entities.Item;
import main.entities.Price;
import main.repositories.ItemRepository;
import main.repositories.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping(path = "")
public class PriceCrudController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PriceRepository priceRepository;

    private ResponseEntity<Price> formErrorMsgPrice(String msg, HttpStatus status) {
        Price err = new Price();
        err.setPrice(null);
        err.setItem(null);
        err.setDate(null);
        err.setId(null);
        err.setErrorMsg(msg);
        return ResponseEntity.status(status).body(err);
    }

    @PostMapping(value = "/add-price")
    public ResponseEntity<Price> addPrice(@RequestBody PriceDTO price) {
        Item item = itemRepository.retrieveItem(price.getItemId());
        Price response;
        // Item was not found
        if (Objects.isNull(item)) {
            return formErrorMsgPrice("No item with given id!", HttpStatus.NOT_FOUND);
        }

        // ClientInvoice with given Id already exist
        if (Objects.nonNull(priceRepository.findPrice(price.getId()))) {
            return formErrorMsgPrice("Price with given Id already exist!", HttpStatus.CONFLICT);
        }

        try {
            // Todo: add validation
            LocalDate date = LocalDate.parse(price.getLocalDate());
             response = priceRepository.addPrice(price.getId(), item, price.getPrice(), date);
        } catch (Exception exception) {
            // not unique invoiceId
            return formErrorMsgPrice("Given price id not unique!", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/get-price/{id}")
    public ResponseEntity<Price> findPrice(@PathVariable Integer id) {
        Price price = priceRepository.findPrice(id);
        return Objects.nonNull(price) ? ResponseEntity.ok(price) :
                formErrorMsgPrice("No price with given id!", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/get-all-prices")
    public ResponseEntity getAllPrices() {
        return ResponseEntity.ok(priceRepository.getAllPrices());
    }

    @GetMapping(value = "/get-all-prices/{itemName}")
    public ResponseEntity getAllPricesByItemName(@PathVariable String itemName) {
        return ResponseEntity.ok(itemRepository.findPricesByItemName(itemName));
    }
}
