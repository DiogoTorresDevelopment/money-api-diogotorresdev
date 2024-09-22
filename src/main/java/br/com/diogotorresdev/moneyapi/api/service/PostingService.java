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
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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


    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private Mailer mailer;

//    Exemplo delay fixo
//    @Scheduled(fixedDelay = 1000 * 2)
//    public void notifyOnPostingsExpired(){
//        System.out.println(">>>>>>>>>>>>>>>>>>>>> Método sendo executado ......");
//    }

    //cron = segundo minuto hora diaDoMes oMes diaDaSemana
    @Scheduled(cron = "0 0 6 * * *")
    public void notifyOnPostingsExpired(){
        List<Posting> expireds = postingRepository
                .findByDueDateLessThanEqualAndPaymentDateIsNull(LocalDate.now());

        List<UserAccount> recipients = userAccountRepository
                .findByPermissionsPermissionDescription(RECIPIENTS);


        mailer.sendEmailNotificationPostingsExpired(expireds, recipients);

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

        BeanUtils.copyProperties(posting, postingSaved, "id");

        postingRepository.save(postingSaved);

        return postingSaved;
    }

    public void delete(Long id) {
        postingRepository.delete(id);
    }
}
