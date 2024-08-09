package br.com.diogotorresdev.moneyapi.api.service;

import br.com.diogotorresdev.moneyapi.api.model.Person;
import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.repository.PersonRepository;
import br.com.diogotorresdev.moneyapi.api.repository.PostingRepository;
import br.com.diogotorresdev.moneyapi.api.repository.filter.PostingFilter;
import br.com.diogotorresdev.moneyapi.api.repository.projection.PostingProjection;
import br.com.diogotorresdev.moneyapi.api.service.exception.InactiveOrNonExistentPersonException;
import br.com.diogotorresdev.moneyapi.api.service.exception.PersonNonexistentOrInactiveException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostingService {

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private PersonService personService;

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
