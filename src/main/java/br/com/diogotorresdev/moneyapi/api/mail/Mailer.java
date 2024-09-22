package br.com.diogotorresdev.moneyapi.api.mail;

import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.model.UserAccount;
import br.com.diogotorresdev.moneyapi.api.repository.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine thymeleaf;

    @Autowired
    private PostingRepository postingRepository;

//    @EventListener
//    private void test(ApplicationReadyEvent event){
//        String template = "mail/notice-posting-expired";
//
//        List<Posting> postings = postingRepository.findAll();
//
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("postings", postings);
//
//
//        this.sendEmail("diogotorresdev@outlook.com.br",
//                Arrays.asList("detangames@gmail.com"), "Testando envio de Email"
//                ,"Ola!<br> Teste ok.");
//
//        System.out.println("Terminado o envio de e-mail!");
//    }

    public void sendEmailNotificationPostingsExpired(
            List<Posting> postings, List<UserAccount> recipients){

        Map<String, Object> variables = new HashMap<>();
        variables.put("postings", postings);

        List<String> emails = recipients.stream()
                .map(u -> u.getEmail()).collect(Collectors.toList());

        this.sendEmail("diogo@emid.com.br",
                emails,
                "Lançamentos Vencidos",
                "mail/notice-posting-expired",
                variables);
    }


    public void sendEmail(
            String from, List<String> to, String subject,
            String template, Map<String, Object> variables){

        Context context = new Context(new Locale("pt","BR"));

        variables.entrySet().forEach(entry -> context.setVariable(entry.getKey(), entry.getValue()));

        String message = thymeleaf.process(template, context);

        this.sendEmail(from, to, subject, message);

    }

    public void sendEmail(String from, List<String> to, String subject, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to.toArray(new String[to.size()]));
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message, true);
        } catch (MessagingException e) {
            throw new RuntimeException("Problemas com o envio de e-mail!", e);
        }
    }


}
