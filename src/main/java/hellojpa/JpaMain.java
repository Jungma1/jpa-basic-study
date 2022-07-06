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
            Address address = new Address("city", "street", "10000");

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setHomeAddress(address);
            em.persist(member1);

            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());

            Member member2 = new Member();
            member2.setUsername("member2");
//            member2.setHomeAddress(address);
            member2.setHomeAddress(copyAddress);
            em.persist(member2);

            // 임베디드 타입 같은 값 타입은 여러 엔티티에서 공유하면 위험함
            // member1 에 city 정보를 변경했지만 member2 에 city 도 같이 변경됨
            // 따라서, *값(인스턴스)를 복사해서 사용하도록 함

            // *불변 객체로 위와 같은 문제를 해결함
//            member1.getHomeAddress().setCity("newCity");
            Address newAddress = new Address("newCity", address.getStreet(), address.getZipcode());
            member1.setHomeAddress(newAddress);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
