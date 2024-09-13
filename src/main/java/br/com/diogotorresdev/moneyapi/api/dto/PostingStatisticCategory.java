package br.com.diogotorresdev.moneyapi.api.dto;

import br.com.diogotorresdev.moneyapi.api.model.Category;

import java.math.BigDecimal;

public class PostingStatisticCategory {

    private Category category;

    private BigDecimal total;

    public PostingStatisticCategory() {
    }

    public PostingStatisticCategory(Category category, BigDecimal total) {
        this.category = category;
        this.total = total;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
