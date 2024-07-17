package br.com.diogotorresdev.moneyapi.api.service;

import br.com.diogotorresdev.moneyapi.api.model.Person;
import br.com.diogotorresdev.moneyapi.api.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person update(Long id,Person person) {
        Person existingPerson = this.searchPersonForId(id);

        BeanUtils.copyProperties(person, existingPerson, "id");

        return personRepository.save(existingPerson);
    }



    public Person updatePropertyActive(Long id,Boolean active) {
        Person existingPerson = this.searchPersonForId(id);
        existingPerson.setActive(active);

        return personRepository.save(existingPerson);
    }


    private Person searchPersonForId(Long id) {
        Person existingPerson = personRepository.findOne(id);
        if(existingPerson == null) {
            throw new EmptyResultDataAccessException(1);
        }
        return existingPerson;
    }

}
