package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Child childA = new Child();
            Child childB = new Child();

            Parent parent = new Parent();
            parent.addChild(childA);
            parent.addChild(childB);

            em.persist(parent); // cascade = CascadeType.ALL - child 도 persist 실행

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());

            // orphanRemoval = true - 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
            findParent.getChildList().remove(0);

            em.remove(findParent);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
