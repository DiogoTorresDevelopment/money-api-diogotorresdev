package br.com.diogotorresdev.moneyapi.api.config;

import br.com.diogotorresdev.moneyapi.api.config.property.MoneyApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Autowired
    private MoneyApiProperty property;

    @Bean
    public JavaMailSender javaMailSender(){
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable",true);
        props.put("mail.smtp.connectiontimeout",10000);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setJavaMailProperties(props);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setHost(property.getMail().getHost());
        mailSender.setPort(property.getMail().getPort());
        mailSender.setUsername(property.getMail().getUsername());
        mailSender.setPassword(property.getMail().getPassword());

        return mailSender;
    }


}
