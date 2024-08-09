package br.com.diogotorresdev.moneyapi.api.repository.projection;

import br.com.diogotorresdev.moneyapi.api.model.enums.PostingType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PostingProjection {

    private Long id;
    private String postingDescription;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private BigDecimal postingValue;
    private PostingType postingType;
    private String category;
    private String person;

    public PostingProjection(Long id, String postingDescription, LocalDate dueDate, LocalDate paymentDate, BigDecimal postingValue, PostingType postingType, String category, String person) {
        this.id = id;
        this.postingDescription = postingDescription;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.postingValue = postingValue;
        this.postingType = postingType;
        this.category = category;
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostingDescription() {
        return postingDescription;
    }

    public void setPostingDescription(String postingDescription) {
        this.postingDescription = postingDescription;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPostingValue() {
        return postingValue;
    }

    public void setPostingValue(BigDecimal postingValue) {
        this.postingValue = postingValue;
    }

    public PostingType getPostingType() {
        return postingType;
    }

    public void setPostingType(PostingType postingType) {
        this.postingType = postingType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
