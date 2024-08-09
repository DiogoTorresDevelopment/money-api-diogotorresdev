package br.com.diogotorresdev.moneyapi.api.resource;

import br.com.diogotorresdev.moneyapi.api.event.RecursoCriadoEvent;
import br.com.diogotorresdev.moneyapi.api.model.Person;
import br.com.diogotorresdev.moneyapi.api.repository.PersonRepository;
import br.com.diogotorresdev.moneyapi.api.service.PersonService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonResource {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private PersonService personService;


    @PreAuthorize("hasAuthority('ROLE_VIEW_PERSON') and #oauth2.hasScope('read')")
    @GetMapping
    public List<Person> list() {
        return personRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_REGISTER_PERSON') and #oauth2.hasScope('write')")
    @PostMapping
    public ResponseEntity<Person> save(@Valid @RequestBody Person person, HttpServletResponse response) {
        Person newPerson = personRepository.save(person);

        publisher.publishEvent(new RecursoCriadoEvent(this,response, newPerson.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(newPerson);
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW_PERSON') and #oauth2.hasScope('read')")
    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        Person person = personRepository.findOne(id);
        return person != null? ResponseEntity.ok(person) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('ROLE_DELETE_PERSON') and #oauth2.hasScope('write')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        personRepository.delete(id);
    }

    @PreAuthorize("hasAuthority('ROLE_UPDATE_PERSON') and #oauth2.hasScope('write')")
    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable Long id, @Valid @RequestBody Person person) {
        Person existingPerson = personService.update(id, person);

        return ResponseEntity.ok(existingPerson);
    }

    @PreAuthorize("hasAuthority('ROLE_UPDATE_PERSON') and #oauth2.hasScope('write')")
    @PutMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePropertyActive(@PathVariable Long id, @RequestBody Boolean active){
        personService.updatePropertyActive(id, active);
    }


}
