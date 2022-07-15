package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.Team;
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
            Team teamA = new Team();
            teamA.setName("TeamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("Member1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("Member2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("Member3");
            member3.setTeam(teamB);
            em.persist(member3);

            /**
             * 벌크 연산 - 쿼리 한 번으로 여러 테이블 로우 변경
             * 벌크 연산 주의점 - 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
             * 해결책 - 1. 벌크 연산을 먼저 실행
             *          2. 벌크 연산 수행 후 영속성 컨텍스트 초기화
             */
            int resultCount = em.createQuery("update j_member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);

            em.clear(); // 영속성 컨텍스트 초기화

            Member findMember = em.find(Member.class, member1.getId()); // DB 에서 가져옴
            System.out.println("findMember = " + findMember.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
