package br.com.diogotorresdev.moneyapi.api.service;

import br.com.diogotorresdev.moneyapi.api.dto.PostingStatisticPerson;
import br.com.diogotorresdev.moneyapi.api.mail.Mailer;
import br.com.diogotorresdev.moneyapi.api.model.Person;
import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.model.UserAccount;
import br.com.diogotorresdev.moneyapi.api.repository.PersonRepository;
import br.com.diogotorresdev.moneyapi.api.repository.PostingRepository;
import br.com.diogotorresdev.moneyapi.api.repository.UserAccountRepository;
import br.com.diogotorresdev.moneyapi.api.repository.filter.PostingFilter;
import br.com.diogotorresdev.moneyapi.api.repository.projection.PostingProjection;
import br.com.diogotorresdev.moneyapi.api.service.exception.InactiveOrNonExistentPersonException;
import br.com.diogotorresdev.moneyapi.api.service.exception.PersonNonexistentOrInactiveException;
import br.com.diogotorresdev.moneyapi.api.storage.S3;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class PostingService {

    private static final String RECIPIENTS = "ROLE_VIEW_POSTING";

    private static final Logger logger = LoggerFactory.getLogger(PostingService.class);

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private Mailer mailer;

    @Autowired
    private S3 s3;
//    Exemplo delay fixo
//    @Scheduled(fixedDelay = 1000 * 2)
//    public void notifyOnPostingsExpired(){
//        System.out.println(">>>>>>>>>>>>>>>>>>>>> Método sendo executado ......");
//    }

    //cron = segundo minuto hora diaDoMes oMes diaDaSemana
    @Scheduled(cron = "0 0 6 * * *")
    public void notifyOnPostingsExpired(){
        if(logger.isDebugEnabled()){
            logger.debug("Preparando Envio de e-mails de aviso de lançamentos vencidos");
        }

        List<Posting> expireds = postingRepository
                .findByDueDateLessThanEqualAndPaymentDateIsNull(LocalDate.now());

        if(expireds.isEmpty()){
            logger.info("Sem lançamentos vencidos para aviso!");

            return;
        }

        logger.info("Existem {} lançamentos vencidos.", expireds.size());

        List<UserAccount> recipients = userAccountRepository
                .findByPermissionsPermissionDescription(RECIPIENTS);

        if(recipients.isEmpty()){
            logger.warn("Existem lançamentos vencidos, mais o sistema não encontrou destinatarios");
            return;
        }


        mailer.sendEmailNotificationPostingsExpired(expireds, recipients);

        logger.info("Envio de e-mail de aviso concluído.");
    }


    public byte[] reportByPerson(LocalDate init, LocalDate end) throws Exception {
        List<PostingStatisticPerson> data = postingRepository.byPerson(init, end);

        Map<String, Object> params = new HashMap<>();
        params.put("DT_INIT", Date.valueOf(init));
        params.put("DT_END", Date.valueOf(end));
        params.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("report/posting-per-person.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params,
                new JRBeanCollectionDataSource(data));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public Posting save(Posting posting) {
        Person person = personService.findPersonPostingById(posting.getPerson().getId());

        if (person == null || person.isInactive()) {
            throw new InactiveOrNonExistentPersonException();
        }

        if(StringUtils.hasText(posting.getFileAttachment())){
            s3.saveFile(posting.getFileAttachment());
        }

        return postingRepository.save(posting);
    }

    public Page<Posting> find(PostingFilter postingFilter, Pageable pageable) {
        return postingRepository.filter(postingFilter, pageable);
    }

    public Page<PostingProjection> projection(PostingFilter postingFilter, Pageable pageable) {
        return postingRepository.projection(postingFilter, pageable);
    }

    public Posting findById(Long id) {
        Posting postingSaved = postingRepository.findOne(id);

        if (postingSaved == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return postingSaved;
    }

    public Posting update(Long id, Posting posting) {
        Posting postingSaved = postingRepository.findOne(id);

        if (postingSaved == null) {
            throw new IllegalArgumentException();
        }

        if (!posting.getPerson().equals(postingSaved.getPerson())) {
            personService.validatePerson(posting.getPerson().getId());
        }

        if(StringUtils.isEmpty(posting.getFileAttachment()) && StringUtils.hasText(posting.getFileAttachment())){
            s3.remove(postingSaved.getFileAttachment());
        }else if(StringUtils.hasText(posting.getFileAttachment()) && !posting.getFileAttachment().equals(postingSaved.getFileAttachment())){
            s3.toReplaceFileAttachment(postingSaved.getFileAttachment(), posting.getFileAttachment());
        }

        BeanUtils.copyProperties(posting, postingSaved, "id");

        postingRepository.save(postingSaved);

        return postingSaved;
    }

    public void delete(Long id) {
        postingRepository.delete(id);
    }
}
