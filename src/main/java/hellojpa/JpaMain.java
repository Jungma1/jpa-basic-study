package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

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

            em.flush();
            em.clear();

            // 지연 로딩
            // Member1, TeamA(SQL)
            // Member2, TeamA(영속성 컨텍스트 - 1차 캐시)
            // Member3, TeamB(SQL)
            // 만약, 회원이 100명이라면 쿼리문 실행이 많아짐 -> N + 1 문제 -> *페치 조인으로 해결
            String query = "select m from j_member m";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
            }

            // 페치 조인으로 회원과 팀을 함께 조회해서 지연 로딩이 없음
            String query2 = "select m from j_member m join fetch m.team";
            List<Member> result2 = em.createQuery(query2, Member.class)
                    .getResultList();

            for (Member member : result2) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
            }

            // 컬렉션 페치 조인
            String query3 = "select t from j_team t join fetch t.members";
            List<Team> result3 = em.createQuery(query3, Team.class)
                    .getResultList();

            for (Team team : result3) {
                System.out.println("team = " + team.getName() + "|" + team.getMembers().size());

                for (Member member : team.getMembers()) {
                    System.out.println("-> member = " + member);
                }
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
