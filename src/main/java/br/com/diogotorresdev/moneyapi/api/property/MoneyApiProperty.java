package br.com.diogotorresdev.moneyapi.api.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("moneyapi")
public class MoneyApiProperty {
//    private final Security security = new Security();

    private String permittedOrigin = "http://localhost:8000";

//    public Security getSecurity() {
//        return security;
//    }
//
//    public String getPermittedOrigin() {
//        return permittedOrigin;
//    }
//
//    public void setPermittedOrigin(String permittedOrigin) {
//        this.permittedOrigin = permittedOrigin;
//    }
//
//    public static class Security {
//        private boolean enableHttps;
//
//        public boolean isEnableHttps() {
//            return enableHttps;
//        }
//
//        public void setEnableHttps(boolean enableHttps) {
//            this.enableHttps = enableHttps;
//        }
//    }
}
