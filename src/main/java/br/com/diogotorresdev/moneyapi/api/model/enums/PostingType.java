package br.com.diogotorresdev.moneyapi.api.model.enums;


public enum PostingType {

    RECEITA("Receita"),
    DESPESA("Despesa");

    private String descricao;

    PostingType(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
