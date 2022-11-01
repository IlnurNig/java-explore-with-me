package ru.practicum.category.repository;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CategoryAuditRepository {

    private final EntityManager em;

    @Autowired
    public CategoryAuditRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Object> test(){
//        id
        AuditReader reader = AuditReaderFactory.get(em);
//        AuditQuery query = reader.createQuery()
//
//                .forRevisionsOfEntity(Category.class, true, true);
//        List<Object> result = query.getResultList();
//
//        return query.getResultList()

        AuditQuery q = reader.createQuery().forRevisionsOfEntity(Category.class, false, true);

        return  q.getResultList();


    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Object> testId(Long id){
//        id
        AuditReader reader = AuditReaderFactory.get(em);
//        AuditQuery query = reader.createQuery()
//
//                .forRevisionsOfEntity(Category.class, true, true);
//        List<Object> result = query.getResultList();
//
//        return query.getResultList()

        AuditQuery q = reader.createQuery().forRevisionsOfEntity(Category.class, false, true);
        q.add(AuditEntity.id().eq(id));
        return  q.getResultList();


    }

}
