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

    private final S3 s3 = new S3();

    public S3 getS3() {
        return s3;
    }

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

    public static class S3 {

        private String accessKeyId;

        private String secretAccessKey;

        private String bucket = "aw-money-api-diogotorresdev-files";

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getSecretAccessKey() {
            return secretAccessKey;
        }

        public void setSecretAccessKey(String secretAccessKey) {
            this.secretAccessKey = secretAccessKey;
        }
    }

}
