package br.com.diogotorresdev.moneyapi.api.repository.posting;

import br.com.diogotorresdev.moneyapi.api.dto.PostingStatisticCategory;
import br.com.diogotorresdev.moneyapi.api.dto.PostingStatisticDay;
import br.com.diogotorresdev.moneyapi.api.dto.PostingStatisticPerson;
import br.com.diogotorresdev.moneyapi.api.model.*;
import br.com.diogotorresdev.moneyapi.api.model.enums.PostingType;
import br.com.diogotorresdev.moneyapi.api.repository.filter.PostingFilter;
import br.com.diogotorresdev.moneyapi.api.repository.projection.PostingProjection;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostingRepositoryImpl implements PostingRepositoryQuery {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PostingStatisticCategory> byCategory(LocalDate monthReference) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostingStatisticCategory> criteria = criteriaBuilder.createQuery(PostingStatisticCategory.class);
        Root<Posting> root = criteria.from(Posting.class);

        criteria.select(criteriaBuilder.construct(PostingStatisticCategory.class,
                root.get(Posting_.category), criteriaBuilder.sum(root.get(Posting_.postingValue))));

        LocalDate firstDayOfMonth = monthReference.withDayOfMonth(1);
        LocalDate lastDayOfMonth = monthReference.withDayOfMonth(monthReference.lengthOfMonth());

        criteria.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Posting_.dueDate), firstDayOfMonth),
                criteriaBuilder.lessThanOrEqualTo(root.get(Posting_.dueDate), lastDayOfMonth));

        criteria.groupBy(root.get(Posting_.category));

        TypedQuery<PostingStatisticCategory> query = entityManager.createQuery(criteria);

        return query.getResultList();
    }

    @Override
    public List<PostingStatisticPerson> byPerson(LocalDate init, LocalDate end) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostingStatisticPerson> criteria = criteriaBuilder.createQuery(PostingStatisticPerson.class);
        Root<Posting> root = criteria.from(Posting.class);

        criteria.select(criteriaBuilder.construct(
                PostingStatisticPerson.class,
                root.get(Posting_.postingType),
                root.get(Posting_.person),
                criteriaBuilder.sum(root.get(Posting_.postingValue))
        ));


        criteria.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Posting_.dueDate), init),
                criteriaBuilder.lessThanOrEqualTo(root.get(Posting_.dueDate), end));

        criteria.groupBy(root.get(Posting_.postingType),root.get(Posting_.person));

        TypedQuery<PostingStatisticPerson> query = entityManager.createQuery(criteria);

        return query.getResultList();
    }

    @Override
    public List<PostingStatisticDay> byDay(LocalDate monthReference) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostingStatisticDay> criteria = criteriaBuilder.createQuery(PostingStatisticDay.class);
        Root<Posting> root = criteria.from(Posting.class);

        criteria.select(criteriaBuilder.construct(
                PostingStatisticDay.class,
                root.get(Posting_.postingType),
                root.get(Posting_.dueDate),
                criteriaBuilder.sum(root.get(Posting_.postingValue))
        ));

        LocalDate firstDayOfMonth = monthReference.withDayOfMonth(1);
        LocalDate lastDayOfMonth = monthReference.withDayOfMonth(monthReference.lengthOfMonth());

        criteria.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Posting_.dueDate), firstDayOfMonth),
                criteriaBuilder.lessThanOrEqualTo(root.get(Posting_.dueDate), lastDayOfMonth));

        criteria.groupBy(root.get(Posting_.postingType),root.get(Posting_.dueDate));

        TypedQuery<PostingStatisticDay> query = entityManager.createQuery(criteria);

        return query.getResultList();
    }

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

    @Override
    public Page<PostingProjection> projection(PostingFilter postingFilter, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostingProjection> criteriaQuery = criteriaBuilder.createQuery(PostingProjection.class);
        Root<Posting> root = criteriaQuery.from(Posting.class);

        criteriaQuery.select(criteriaBuilder.construct(PostingProjection.class,
                root.get(Posting_.id),root.get(Posting_.postingDescription),
                root.get(Posting_.dueDate),root.get(Posting_.paymentDate),
                root.get(Posting_.postingValue), root.get(Posting_.postingType),
                root.get(Posting_.category).get(Category_.categoryName),
                root.get(Posting_.person).get(Person_.personName)));

        Predicate[] predicates = createRestrictions(postingFilter, criteriaBuilder, root);
        criteriaQuery.where(predicates);

        TypedQuery<PostingProjection> typedQuery = entityManager.createQuery(criteriaQuery);
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
