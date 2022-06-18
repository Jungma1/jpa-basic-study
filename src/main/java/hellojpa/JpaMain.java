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
            // 비영속 - 엔티티를 생성한 상태
            Member member = new Member();
            member.setId(101L);
            member.setName("HelloJPA");

            System.out.println("==== BEFORE ====");
            // 엔티티를 영속성 컨텍스트에 영속
            // em.persist(member);
            System.out.println("==== AFTER ====");

            // DB가 아닌 영속성 컨텍스트 '1차 캐시'에서 조회해서 가져옴
            Member findMember = em.find(Member.class, 101L);
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());

            // 영속 엔티티의 동일성 보장
            Member a = em.find(Member.class, 101L);
            Member b = em.find(Member.class, 101L);
            System.out.println("result = " + (a == b));

            /**
             *  배치
             *  persistence.xml
             *  <property name="hibernate.jdbc.batch_size" value="10"/>
             */
            Member memberA = new Member(150L, "A");
            Member memberB = new Member(160L, "B");
//            em.persist(memberA);
//            em.persist(memberB);
            System.out.println("=======================");

            Member member1 = em.find(Member.class, 150L);
            member1.setName("MBJ");

            // 실제로 DB에 저장되는 시점
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
