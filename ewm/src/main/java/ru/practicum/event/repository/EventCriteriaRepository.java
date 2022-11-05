package ru.practicum.event.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Criteria;
import ru.practicum.event.model.Event;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Repository
public class EventCriteriaRepository {

    private final EntityManager em;

    @Autowired
    public EventCriteriaRepository(EntityManager em) {
        this.em = em;
    }

    public List<Event> findAllByCriteria(Criteria criteria) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        List<Predicate> predicateList = new ArrayList<>();

        if (isNotEmpty(criteria.getUsers())) {
            predicateList.add(criteriaBuilder.in(eventRoot.get("initiator").get("id")).value(criteria.getUsers()));
        }
        if (isNotEmpty(criteria.getStates())) {
            predicateList.add(criteriaBuilder.in(eventRoot.get("state")).value(criteria.getStates()));
        }
        if (isNotEmpty(criteria.getCategories())) {
            predicateList.add(criteriaBuilder.in(eventRoot.get("category").get("id")).value(criteria.getCategories()));
        }
        if (criteria.getPaid() != null) {
            predicateList.add(criteriaBuilder.equal(eventRoot.get("paid"), criteria.getPaid()));
        }
        if (criteria.getRangeStart() != null) {
            predicateList.add(criteriaBuilder.greaterThan(eventRoot.get("eventDate"), criteria.getRangeStart()));
        }
        if (criteria.getRangeEnd() != null) {
            predicateList.add(criteriaBuilder.lessThan(eventRoot.get("eventDate"), criteria.getRangeEnd()));
        }
        if (criteria.getText() != null) {
            predicateList.add(
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.upper(eventRoot.get("annotation")),
                                    "%" + criteria.getText().toUpperCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.upper(eventRoot.get("description")),
                                    "%" + criteria.getText().toUpperCase() + "%"))
            );
        }
        Predicate[] predicatesArr = predicateList.toArray(predicateList.toArray(new Predicate[]{}));

        return em.createQuery(criteriaQuery.select(eventRoot).where(criteriaBuilder.and(predicatesArr)))
                .setFirstResult(criteria.getFrom())
                .setMaxResults(criteria.getSize())
                .getResultList();
    }

}
