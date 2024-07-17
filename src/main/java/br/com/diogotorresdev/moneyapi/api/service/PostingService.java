package br.com.diogotorresdev.moneyapi.api.service;

import br.com.diogotorresdev.moneyapi.api.model.Person;
import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.repository.PersonRepository;
import br.com.diogotorresdev.moneyapi.api.repository.PostingRepository;
import br.com.diogotorresdev.moneyapi.api.service.exception.PersonNonexistentOrInactiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostingService {

    @Autowired
    private PostingRepository postRepository;

    @Autowired
    private PersonRepository personRepository;

    public Posting savePosting(Posting posting) {
        Person person = personRepository.findOne(posting.getPerson().getId());

        if(person == null || person.isInactive()) {
            throw new PersonNonexistentOrInactiveException();
        }

        return postRepository.save(posting);

    }

}
