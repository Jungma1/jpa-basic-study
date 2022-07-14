package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자1");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            // 상태 필드(state field): 경로 탐색의 끝, 탐색X
            String query = "select m.username from j_member m";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();

            result.forEach(System.out::println);

            // 단일 값 연관 경로: *묵시적 내부 조인(inner join) 발생, 탐색O
            String query2 = "select m.team from j_member m";
            List<Team> result2 = em.createQuery(query2, Team.class)
                    .getResultList();

            result2.forEach(System.out::println);

            // 컬렉션 값 연관 경로: 묵시적 내부 조인 발생(실무X), 탐색X
            String query3 = "select t.members from j_team t";
            List<Collection> result3 = em.createQuery(query3, Collection.class)
                    .getResultList();

            System.out.println("result3 = " + result3);

            // 묵시적 조인 -> *명시적 조인을 사용: FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능
            String query4 = "select m from j_team t join t.members m";
            List<Member> result4 = em.createQuery(query4, Member.class)
                    .getResultList();

            result4.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
