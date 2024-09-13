package br.com.diogotorresdev.moneyapi.api.dto;

import br.com.diogotorresdev.moneyapi.api.model.enums.PostingType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PostingStatisticDay {

    private PostingType postingType;

    private LocalDate day;

    private BigDecimal amount;

    public PostingStatisticDay() {
    }

    public PostingStatisticDay(PostingType postingType, LocalDate day, BigDecimal amount) {
        this.postingType = postingType;
        this.day = day;
        this.amount = amount;
    }

    public PostingType getPostingType() {
        return postingType;
    }

    public void setPostingType(PostingType postingType) {
        this.postingType = postingType;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}