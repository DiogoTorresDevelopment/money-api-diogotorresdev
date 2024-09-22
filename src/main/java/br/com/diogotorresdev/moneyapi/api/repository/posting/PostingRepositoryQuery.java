package br.com.diogotorresdev.moneyapi.api.repository.posting;

import br.com.diogotorresdev.moneyapi.api.dto.PostingStatisticCategory;
import br.com.diogotorresdev.moneyapi.api.dto.PostingStatisticDay;
import br.com.diogotorresdev.moneyapi.api.dto.PostingStatisticPerson;
import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.repository.filter.PostingFilter;
import br.com.diogotorresdev.moneyapi.api.repository.projection.PostingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PostingRepositoryQuery {

    public List<PostingStatisticPerson> byPerson(LocalDate init, LocalDate end);

    public List<PostingStatisticCategory> byCategory(LocalDate monthReference);

    public List<PostingStatisticDay> byDay(LocalDate monthReference);

    public Page<Posting> filter(PostingFilter postingFilter, Pageable pageable);

    public Page<PostingProjection> projection(PostingFilter postingFilter, Pageable pageable);

}
