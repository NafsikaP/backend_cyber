package gr.hua.dit.aimodotes.demo.dao;

import gr.hua.dit.aimodotes.demo.entity.Secretary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SecretaryDAOImpl implements SecretaryDAO{

    @PersistenceContext
    private EntityManager entityManager;

    //get all secretaries
    @Override
    @Transactional
    public List<Secretary> getSecretaries() {
        TypedQuery<Secretary> query = entityManager.createQuery("from Secretary", Secretary.class);
        return query.getResultList();
    }

    //get one secretary
    @Override
    public Secretary getSecretary(Integer secretary_id) {
        return entityManager.find(Secretary.class,secretary_id);
    }

    //save one secretary
    @Override
    @Transactional
    public Secretary saveSecretary(Secretary secretary) {
        if (secretary.getId() == null) {
            entityManager.persist(secretary);
        } else {
            entityManager.merge(secretary);
        }
        return secretary;
    }

    //delete one secretary
    @Override
    @Transactional
    public void deleteSecretary(Integer secretary_id) {
        System.out.println("Deleting secretary with id: " + secretary_id);
        entityManager.remove(entityManager.find(Secretary.class, secretary_id));
    }

}
