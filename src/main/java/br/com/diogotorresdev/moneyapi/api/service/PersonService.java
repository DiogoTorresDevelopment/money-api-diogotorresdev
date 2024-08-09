package br.com.diogotorresdev.moneyapi.api.service;

import br.com.diogotorresdev.moneyapi.api.model.Person;
import br.com.diogotorresdev.moneyapi.api.repository.PersonRepository;
import br.com.diogotorresdev.moneyapi.api.service.exception.InactiveOrNonExistentPersonException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person findById(Long id) {
        Person personSaved = personRepository.findOne(id);

        if (personSaved == null) {
            throw new IllegalArgumentException();
        }

        return personSaved;
    }

    public Page<Person> find(String personName, Pageable pageable) {
        Page<Person> personSaved = personRepository.findByPersonNameContaining(personName, pageable);

        return personSaved;
    }

    public Person findPersonPostingById(Long id) {
        Person personSaved = personRepository.findOne(id);

        return personSaved;
    }

    public Person update(Long id, Person person) {
        Person personSaved = findById(id);

        BeanUtils.copyProperties(person, personSaved, "id");

        personRepository.save(personSaved);

        return personSaved;
    }

    public void updatePropertyActive(Long id, Boolean active) {
        Person personSaved = findById(id);

        personSaved.setActive(active);

        personRepository.save(personSaved);
    }

    public void validatePerson(Long id) {
        Person personSaved = null;

        if (id != null) {
            personSaved = personRepository.findOne(id);
        }

        if (personSaved == null || personSaved.isInactive()) {
            throw new InactiveOrNonExistentPersonException();
        }
    }
}
