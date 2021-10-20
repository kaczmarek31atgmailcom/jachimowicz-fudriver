package com.fungisearch.fudriver.type.command.repository;

import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeGroup;
import com.fungisearch.fudriver.type.command.model.TypeSize;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcin on 21.06.16.
 */
@Repository
public class TypeHibernateRepository implements TypeRepository {


    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Type> findAll() {
        Query query = em.createQuery("select t from Type t");
        List<Type> result = new ArrayList<>();
        result.addAll((List<Type>)query.getResultList());
        return result;
    }

    @Override
    public Type findById(Long id) {
        return em.find(Type.class, id);
    }

    @Override
    public void save(Type type) {
        em.persist(type);
    }

    @Override
    public void save(TypeGroup typeGroup) {
        em.persist(typeGroup);
    }

    @Override
    public TypeGroup findTypeGroup(long id) {
        return em.find(TypeGroup.class, id);
    }

    @Override
    public void save(TypeSize typeSize) {
        em.persist(typeSize);
    }

    @Override
    public TypeSize findTypeSize(Long id) {
        return em.find(TypeSize.class, id);
    }
}
