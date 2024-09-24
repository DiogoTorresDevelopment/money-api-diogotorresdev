package br.com.diogotorresdev.moneyapi.api.dto;

public class FileAttachment {

    private String name;

    private String url;

    public FileAttachment(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }



}
