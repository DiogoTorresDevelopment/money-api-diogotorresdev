package br.com.diogotorresdev.moneyapi.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("moneyapi")
public class MoneyApiProperty {

    private final Security security = new Security();

    private String permittedOrigin = "http://localhost:8000";

    public Security getSecurity() {
        return security;
    }

    private final Mail mail = new Mail();

    public Mail getMail(){
        return mail;
    }

    public String getPermittedOrigin() {
        return permittedOrigin;
    }

    public void setPermittedOrigin(String permittedOrigin) {
        this.permittedOrigin = permittedOrigin;
    }

    public static class Security {
        private boolean enableHttps;

        public boolean isEnableHttps() {
            return enableHttps;
        }

        public void setEnableHttps(boolean enableHttps) {
            this.enableHttps = enableHttps;
        }
    }

    public static class Mail {

        private String host;

        private Integer port;

        private String username;

        private String password;



        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
