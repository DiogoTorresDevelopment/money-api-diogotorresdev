package br.com.diogotorresdev.moneyapi.api.resource;

import br.com.diogotorresdev.moneyapi.api.event.RecursoCriadoEvent;
import br.com.diogotorresdev.moneyapi.api.model.Person;
import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.repository.PostingRepository;
import br.com.diogotorresdev.moneyapi.api.service.PostingService;
import br.com.diogotorresdev.moneyapi.api.service.exception.PersonNonexistentOrInactiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.diogotorresdev.moneyapi.api.exceptionhandler.MoneyApiExceptionHandler.Error;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/posting")
public class PostingResource {

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private PostingService postingService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public List<Posting> list() {
        return postingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Posting> getById(@PathVariable Long id) {
        Posting existingPosting = postingRepository.findOne(id);

        return existingPosting!= null? ResponseEntity.ok(existingPosting) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Posting> save(@Valid @RequestBody Posting posting, HttpServletResponse response) {
        Posting newPosting = postingService.savePosting(posting);
        publisher.publishEvent(new RecursoCriadoEvent(this,response, newPosting.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(newPosting);
    }

    @ExceptionHandler({ PersonNonexistentOrInactiveException.class })
    public ResponseEntity<Object> handlePersonNonexistentOrInactive(PersonNonexistentOrInactiveException ex) {
        String userMessage = messageSource.getMessage("person.inactive-or-non-existent", null,LocaleContextHolder.getLocale());
        String devMessage = ex.toString();
        List<Error> errors = Arrays.asList(new Error(userMessage, devMessage));
        return ResponseEntity.badRequest().body(errors);
    }


}
