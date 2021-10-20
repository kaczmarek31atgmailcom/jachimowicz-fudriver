package com.fungisearch.fudriver.person.person.command.repository;

import com.fungisearch.fudriver.person.person.command.model.ForeignerAlert;
import com.fungisearch.fudriver.person.person.command.model.Person;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by idea on 29.02.16.
 */
@Repository
public class PersonHibernateRepository implements PersonRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public void save(Person person) {
        em.persist(person);
      }

    @Override
    public Person find(Long id) {
        return em.find(Person.class,id);
    }


    @Override
    public Boolean isRfidUnique(String rfid) {

        Query query = em.createQuery("select id from Person where rfid =:rfid");
        query.setParameter("rfid", rfid);
        return query.getResultList().isEmpty();
    }

    @Override
    public Person findPersonByRfid(String rfid) {
        Person result = null;
        Query query = em.createQuery("SELECT p FROM Person p WHERE p.rfid =:rfid");
        query.setParameter(new String("rfid"),rfid);
        List<Person> list = query.getResultList();
        if(!list.isEmpty()){
            result = list.get(0);
        }
        return result;
    }

    @Override
    public List<Person> findAll() {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        return query.getResultList();
    }

    @Override
    public List<Long> findReservedNumbers() {
        Query query = em.createQuery("select p.nr from Person p where p.nr > 0");
        return query.getResultList();
    }

    @Override
    public ForeignerAlert getForeignerAlert() {
        return em.find(ForeignerAlert.class,new Long(1));
    }
}