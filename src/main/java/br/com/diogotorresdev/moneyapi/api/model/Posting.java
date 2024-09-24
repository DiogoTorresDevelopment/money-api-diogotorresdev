package br.com.diogotorresdev.moneyapi.api.model;

import br.com.diogotorresdev.moneyapi.api.model.enums.PostingType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "posting")
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "posting_description")
    private String postingDescription;

    @NotNull
    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @NotNull
    @Column(name = "posting_value")
    private BigDecimal postingValue;

    private String note;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostingType postingType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnoreProperties("contacts")
    @NotNull
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "file_attachment")
    private String fileAttachment;

    @Transient
    private String urlFileAttachment;

    @JsonIgnore
    public boolean isRevenue(){
        return PostingType.RECEITA.equals(this.postingType);
    }

    public Posting() {
    }

    public Posting(Long id, String postingDescription, LocalDate dueDate, LocalDate paymentDate,
                   BigDecimal postingValue, String note, PostingType postingType, Category category, Person person) {
        this.id = id;
        this.postingDescription = postingDescription;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.postingValue = postingValue;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public PostingType getPostingType() {
        return postingType;
    }

    public void setPostingType(PostingType postingType) {
        this.postingType = postingType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Posting other = (Posting) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getFileAttachment() {
        return fileAttachment;
    }

    public void setFileAttachment(String fileAttachment) {
        this.fileAttachment = fileAttachment;
    }

    public String getUrlFileAttachment() {
        return urlFileAttachment;
    }

    public void setUrlFileAttachment(String urlFileAttachment) {
        this.urlFileAttachment = urlFileAttachment;
    }
}
