package ru.practicum.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Criteria;
import ru.practicum.model.Stats;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Repository
public class StatsCriteriaRepository {

    private final EntityManager em;

    @Autowired
    public StatsCriteriaRepository(EntityManager em) {
        this.em = em;
    }

    public List<Stats> findAllByCriteria(Criteria criteria) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Stats> criteriaQuery = criteriaBuilder.createQuery(Stats.class);
        Root<Stats> statsRoot = criteriaQuery.from(Stats.class);
        List<Predicate> predicateList = new ArrayList<>();

        if (isNotEmpty(criteria.getUris())) {
            predicateList.add(criteriaBuilder.in(statsRoot.get("uri")).value(criteria.getUris()));
        }
        if (criteria.getStart() != null) {
            predicateList.add(criteriaBuilder.greaterThan(
                    statsRoot.get("createdOn"),
                    criteria.getStart()));
        }
        if (criteria.getEnd() != null) {
            predicateList.add(criteriaBuilder.lessThan(
                    statsRoot.get("createdOn"),
                    criteria.getEnd()));
        }
        if (criteria.getApp() != null) {
            predicateList.add(criteriaBuilder.equal(
                    criteriaBuilder.upper(statsRoot.get("app")),
                    criteria.getApp().toUpperCase()));
        }

        Predicate[] predicatesArr = predicateList.toArray(predicateList.toArray(new Predicate[]{}));

        return em.createQuery(criteriaQuery.select(statsRoot).where(criteriaBuilder.and(predicatesArr)))
                .getResultList();
    }
}
