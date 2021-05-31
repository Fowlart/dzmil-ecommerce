package main.controllers;

import main.entities.Item;
import main.repositories.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(path = "")
public class ItemCrudController {

    public static final String NO_ITEM_WITH_GIVEN_ID = "No item with given id!";
    private final ItemRepository itemRepository;

    public ItemCrudController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @PostMapping(value = "/create-item")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item body = itemRepository.addItem(item.getId(), item.getName());
        if (Objects.isNull(body)) {
            body = new Item();
            body.setId(-1);
            body.setErrorMsg("Item with id " + item.getId() + " is already exist");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping(value = "/update-item")
    public ResponseEntity<Item> updateItem(@RequestBody Item item) {
        Item updatedItem = itemRepository.updateItem(item);
        if (Objects.isNull(updatedItem)) {
            item.setErrorMsg(NO_ITEM_WITH_GIVEN_ID);
        }
        return ResponseEntity.ok(item);
    }

    @GetMapping(value = "/get-item/{id}")
    public ResponseEntity<Item> getItem(@PathVariable("id") Integer id) {
        Item body = itemRepository.retrieveItem(id);
        if (Objects.isNull(body)) {
            body = new Item();
            body.setId(-1);
            body.setErrorMsg(NO_ITEM_WITH_GIVEN_ID);
        }
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "/get-all-items")
    public ResponseEntity getAllItems() {
        return ResponseEntity.ok(itemRepository.getAllItems());
    }

    @DeleteMapping(value = "/remove-item/{id}")
    public ResponseEntity removeItem(@PathVariable("id") Integer id) {
        boolean isDeleted = itemRepository.removeItem(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
