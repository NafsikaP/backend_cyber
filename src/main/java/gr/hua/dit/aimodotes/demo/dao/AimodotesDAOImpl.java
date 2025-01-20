package gr.hua.dit.aimodotes.demo.dao;

import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AimodotesDAOImpl implements AimodotisDAO{

    @PersistenceContext
    private EntityManager entityManager;

    //get all blood donors
    @Override
    @Transactional
    public List<Aimodotis> getAimodotes() {
        TypedQuery<Aimodotis> query = entityManager.createQuery("from Aimodotis", Aimodotis.class);
        return query.getResultList();
    }

    //get one blood donor
    @Override
    public Aimodotis getAimodotis(Integer aimodotis_id) {
        return entityManager.find(Aimodotis.class,aimodotis_id);
    }

    //save blood donor
    @Override
    @Transactional
    public Aimodotis saveAimodotis(Aimodotis aimodotis) {
        System.out.println("Aimodotis "+ aimodotis.getId());
        if (aimodotis.getId() == null) {
            entityManager.persist(aimodotis);
        } else {
            entityManager.merge(aimodotis);
        }
        return aimodotis;
    }

    //delete one blood donor
    @Override
    @Transactional
    public void deleteAimodotis(Integer aimodotis_id) {
        System.out.println("Deleting aimodotis with id: " + aimodotis_id);
        entityManager.remove(entityManager.find(Aimodotis.class, aimodotis_id));
    }
}
