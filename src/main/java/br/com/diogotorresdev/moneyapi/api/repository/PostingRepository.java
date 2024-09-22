package br.com.diogotorresdev.moneyapi.api.repository;

import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.repository.posting.PostingRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long>, PostingRepositoryQuery {

    List<Posting> findByDueDateLessThanEqualAndPaymentDateIsNull(LocalDate data);

}
