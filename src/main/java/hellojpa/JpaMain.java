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
            /**
             * 플러시
             * - 영속성 컨텍스트를 비우지 않음
             * - 영속성 컨텍스트의 변경 내용을 데이터베이스의 동기화
             * - 트랜잭션이 있기 때문에 가능 -> 커밋 직전에만 동기화 하면 됨
             */
            Member member = new Member(200L, "member200");
            // em.persist(member);
            // em.flush(); // 플러시 강제호출 - 쓰기 지연 SQL 저장소 바로 DB 반영, 1차 캐시는 유지

            /**
             * 준영속 상태
             * - 영속성 컨텍스트에서 분리, JPA 에서 더 이상 관리하지 않음
             */
            Member member1 = em.find(Member.class, 150L); // 영속
            member1.setName("AAAAA");
            // em.detach(member1); // 준영속 - update 쿼리문이 실행 안됨
            em.clear();

            System.out.println("====================================");
            tx.commit(); // 실제로 DB에 저장되는 시점
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
