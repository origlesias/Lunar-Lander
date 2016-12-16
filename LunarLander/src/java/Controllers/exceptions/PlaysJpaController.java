

package Controllers.exceptions;

// @author: Oriol Iglesias

import Controllers.exceptions.exceptions.NonexistentEntityException;
import Entities.Plays;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Users;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;


public class PlaysJpaController implements Serializable {

    public PlaysJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Plays plays) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users userId = plays.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                plays.setUserId(userId);
            }
            em.persist(plays);
            if (userId != null) {
                userId.getPlaysCollection().add(plays);
                userId = em.merge(userId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Plays plays) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Plays persistentPlays = em.find(Plays.class, plays.getId());
            Users userIdOld = persistentPlays.getUserId();
            Users userIdNew = plays.getUserId();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                plays.setUserId(userIdNew);
            }
            plays = em.merge(plays);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getPlaysCollection().remove(plays);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getPlaysCollection().add(plays);
                userIdNew = em.merge(userIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = plays.getId();
                if (findPlays(id) == null) {
                    throw new NonexistentEntityException("The plays with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Plays plays;
            try {
                plays = em.getReference(Plays.class, id);
                plays.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plays with id " + id + " no longer exists.", enfe);
            }
            Users userId = plays.getUserId();
            if (userId != null) {
                userId.getPlaysCollection().remove(plays);
                userId = em.merge(userId);
            }
            em.remove(plays);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Plays> findPlaysEntities() {
        return findPlaysEntities(true, -1, -1);
    }

    public List<Plays> findPlaysEntities(int maxResults, int firstResult) {
        return findPlaysEntities(false, maxResults, firstResult);
    }

    private List<Plays> findPlaysEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Plays.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Plays findPlays(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Plays.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaysCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Plays> rt = cq.from(Plays.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void addScore(Users id, Date start, Date finish, int score){
        EntityManager em = getEntityManager();
  Plays a = new Plays();
  EntityTransaction tx = em.getTransaction();
  a.setUserId(id);
  a.setScore(score);
  a.setStartDate(start);
  a.setFinishDate(finish);
  try {
   tx.begin();
   em.persist(a);
   tx.commit();
  } catch (Exception e) {
   if (tx.isActive()) tx.rollback();
   e.printStackTrace();
  }
 }

}
