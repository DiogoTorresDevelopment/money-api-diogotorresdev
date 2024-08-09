package br.com.diogotorresdev.moneyapi.api.repository.posting;

import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.repository.filter.PostingFilter;
import br.com.diogotorresdev.moneyapi.api.repository.projection.PostingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostingRepositoryQuery {

    public Page<Posting> filter(PostingFilter postingFilter, Pageable pageable);

    public Page<PostingProjection> projection(PostingFilter postingFilter, Pageable pageable);

}
