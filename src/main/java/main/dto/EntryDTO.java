package main.dto;

import java.math.BigDecimal;

public class EntryDTO {

    private Integer id;

    private Integer itemId;

    private Integer qty;

    private Integer clientInvoiceId;

    private BigDecimal sellPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getClientInvoiceId() {
        return clientInvoiceId;
    }

    public void setClientInvoiceId(Integer clientInvoiceId) {
        this.clientInvoiceId = clientInvoiceId;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }
}
