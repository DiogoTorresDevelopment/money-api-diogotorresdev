package br.com.diogotorresdev.moneyapi.api.repository.posting;

import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.repository.filter.PostingFilter;
import br.com.diogotorresdev.moneyapi.api.model.Posting_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PostingRepositoryImpl implements PostingRepositoryQuery {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Posting> filter(PostingFilter postingFilter, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Posting> criteriaQuery = criteriaBuilder.createQuery(Posting.class);

        //ADICIONANDO OS FILTROS
        Root<Posting> root = criteriaQuery.from(Posting.class);

        //CRIAR AS RESTRIÇOES
        Predicate[] predicates = createRestrictions(postingFilter, criteriaBuilder, root);
        criteriaQuery.where(predicates);

        TypedQuery<Posting> typedQuery = entityManager.createQuery(criteriaQuery);

        addPaginationRestrictions(typedQuery, pageable);

        return new PageImpl<>(typedQuery.getResultList(), pageable, total(postingFilter));
    }

    private Predicate[] createRestrictions(PostingFilter postingFilter, CriteriaBuilder builder, Root<Posting> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(postingFilter.getPostingDescription())) {
            predicates.add(builder.like(builder.lower(root.get(Posting_.postingDescription).as(String.class)),
                    "%" + postingFilter.getPostingDescription().toLowerCase() + "%"));
        }

        if (postingFilter.getDueDateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(Posting_.dueDate), postingFilter.getDueDateFrom()));
        }

        if (postingFilter.getDueDateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(Posting_.dueDate), postingFilter.getDueDateTo()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void addPaginationRestrictions(TypedQuery<?> query, Pageable pageable) {
        int currentPage = pageable.getPageNumber();
        int totalRecordsPerPage = pageable.getPageSize();
        int firstRecordOfPage = currentPage * totalRecordsPerPage;

        query.setFirstResult(firstRecordOfPage);
        query.setMaxResults(totalRecordsPerPage);
    }

    private long total(PostingFilter postingFilter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Posting> root = criteriaQuery.from(Posting.class);

        Predicate[] predicates = createRestrictions(postingFilter, criteriaBuilder, root);
        criteriaQuery.where(predicates);
        criteriaQuery.select(criteriaBuilder.count(root));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
