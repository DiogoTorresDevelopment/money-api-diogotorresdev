package br.com.diogotorresdev.moneyapi.api.dto;

import br.com.diogotorresdev.moneyapi.api.model.Person;
import br.com.diogotorresdev.moneyapi.api.model.enums.PostingType;

import java.math.BigDecimal;

public class PostingStatisticPerson {

    private PostingType postingType;

    private Person person;

    private BigDecimal total;

    public PostingStatisticPerson() {
    }

    public PostingStatisticPerson(PostingType postingType, Person person, BigDecimal total) {
        this.postingType = postingType;
        this.person = person;
        this.total = total;
    }

    public PostingType getPostingType() {
        return postingType;
    }

    public void setPostingType(PostingType postingType) {
        this.postingType = postingType;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
