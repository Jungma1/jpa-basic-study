package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.hibernate.Hibernate;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("hello");

            em.persist(member);
            em.flush();
            em.clear();

//            Member findMember = em.find(Member.class, member.getId());

            // em.getReference() - 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회
            Member findMember = em.getReference(Member.class, member.getId());

            // findMember = class hellojpa.Member$HibernateProxy$6wJulIUS
            System.out.println("findMember = " + findMember.getClass());
//            System.out.println("findMember.getId() = " + findMember.getId());
//            System.out.println("findMember.getUsername() = " + findMember.getUsername());

            // 프록시 강제 초기화 *(JPA 표준은 강제 초기화 없음)
            Hibernate.initialize(findMember);

            // 프록시 인스턴스의 초기화 여부 확인
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(findMember));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
