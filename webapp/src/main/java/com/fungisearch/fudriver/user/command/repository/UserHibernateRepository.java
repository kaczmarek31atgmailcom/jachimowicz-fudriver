package com.fungisearch.fudriver.user.command.repository;

import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.command.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcin on 25.03.16.
 */
@Repository
public class UserHibernateRepository implements UserRepository {

    @PersistenceContext
    EntityManager em;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void save(User user) {
        em.persist(user);
        em.flush();
    }

    @Override
    public void saveUserRole(UserRole userRole) {
        em.persist(userRole);
        em.flush();
    }

    @Override
    public void update(User user) {
        em.merge(user);
        em.flush();
    }


    @Override
    public void delete(UserRole userRole) {
        em.remove(userRole);
        em.flush();
    }

    @Override
    public UserRole findByUserIdAndRoleName(Long userId, String roleName) {
        UserRole result = null;
        Query query = em.createQuery("from UserRole where userId = :userId and roleName = :roleName");
        query.setParameter(new String("userId"), userId);
        query.setParameter(new String("roleName"), roleName);
        List<UserRole> roles = query.getResultList();
        if (roles.size() == 1) {
            result = (UserRole) query.getResultList().get(0);
        }
        return result;
    }

    @Override
    public User getReference(Long userId) {
        return em.getReference(User.class, userId);
    }

    @Override
    public User find(Long userId) {
        return em.find(User.class, userId);
    }

    @Override
    public List<UserRole> findUserRoles(Long userId) {
        List<UserRole> roles = new ArrayList<UserRole>();
        Query query = em.createQuery("from UserRole where userId =:userId");
        query.setParameter(new String("userId"), userId);
        roles = query.getResultList();
        return roles;
    }

}
