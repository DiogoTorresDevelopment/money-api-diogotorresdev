package br.com.diogotorresdev.moneyapi.api.repository.filter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PostingFilter {


    private String postingDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDateFrom;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDateTo;



    public String getPostingDescription() {
        return postingDescription;
    }

    public void setPostingDescription(String postingDescription) {
        this.postingDescription = postingDescription;
    }

    public LocalDate getDueDateFrom() {
        return dueDateFrom;
    }

    public void setDueDateFrom(LocalDate dueDateFrom) {
        this.dueDateFrom = dueDateFrom;
    }

    public LocalDate getDueDateTo() {
        return dueDateTo;
    }

    public void setDueDateTo(LocalDate dueDateTo) {
        this.dueDateTo = dueDateTo;
    }

}
