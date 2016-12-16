

package Controllers.exceptions;

// @author: Oriol Iglesias

import Controllers.exceptions.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.Plays;
import Entities.Users;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;


public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        if (users.getPlaysCollection() == null) {
            users.setPlaysCollection(new ArrayList<Plays>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Plays> attachedPlaysCollection = new ArrayList<Plays>();
            for (Plays playsCollectionPlaysToAttach : users.getPlaysCollection()) {
                playsCollectionPlaysToAttach = em.getReference(playsCollectionPlaysToAttach.getClass(), playsCollectionPlaysToAttach.getId());
                attachedPlaysCollection.add(playsCollectionPlaysToAttach);
            }
            users.setPlaysCollection(attachedPlaysCollection);
            em.persist(users);
            for (Plays playsCollectionPlays : users.getPlaysCollection()) {
                Users oldUserIdOfPlaysCollectionPlays = playsCollectionPlays.getUserId();
                playsCollectionPlays.setUserId(users);
                playsCollectionPlays = em.merge(playsCollectionPlays);
                if (oldUserIdOfPlaysCollectionPlays != null) {
                    oldUserIdOfPlaysCollectionPlays.getPlaysCollection().remove(playsCollectionPlays);
                    oldUserIdOfPlaysCollectionPlays = em.merge(oldUserIdOfPlaysCollectionPlays);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getId());
            Collection<Plays> playsCollectionOld = persistentUsers.getPlaysCollection();
            Collection<Plays> playsCollectionNew = users.getPlaysCollection();
            Collection<Plays> attachedPlaysCollectionNew = new ArrayList<Plays>();
            for (Plays playsCollectionNewPlaysToAttach : playsCollectionNew) {
                playsCollectionNewPlaysToAttach = em.getReference(playsCollectionNewPlaysToAttach.getClass(), playsCollectionNewPlaysToAttach.getId());
                attachedPlaysCollectionNew.add(playsCollectionNewPlaysToAttach);
            }
            playsCollectionNew = attachedPlaysCollectionNew;
            users.setPlaysCollection(playsCollectionNew);
            users = em.merge(users);
            for (Plays playsCollectionOldPlays : playsCollectionOld) {
                if (!playsCollectionNew.contains(playsCollectionOldPlays)) {
                    playsCollectionOldPlays.setUserId(null);
                    playsCollectionOldPlays = em.merge(playsCollectionOldPlays);
                }
            }
            for (Plays playsCollectionNewPlays : playsCollectionNew) {
                if (!playsCollectionOld.contains(playsCollectionNewPlays)) {
                    Users oldUserIdOfPlaysCollectionNewPlays = playsCollectionNewPlays.getUserId();
                    playsCollectionNewPlays.setUserId(users);
                    playsCollectionNewPlays = em.merge(playsCollectionNewPlays);
                    if (oldUserIdOfPlaysCollectionNewPlays != null && !oldUserIdOfPlaysCollectionNewPlays.equals(users)) {
                        oldUserIdOfPlaysCollectionNewPlays.getPlaysCollection().remove(playsCollectionNewPlays);
                        oldUserIdOfPlaysCollectionNewPlays = em.merge(oldUserIdOfPlaysCollectionNewPlays);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            Collection<Plays> playsCollection = users.getPlaysCollection();
            for (Plays playsCollectionPlays : playsCollection) {
                playsCollectionPlays.setUserId(null);
                playsCollectionPlays = em.merge(playsCollectionPlays);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void addUser(String nick, String password){
        EntityManager em = getEntityManager();
  Users a = new Users();
  EntityTransaction tx = em.getTransaction();
  a.setNick(nick);
  a.setPassword(password);
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
