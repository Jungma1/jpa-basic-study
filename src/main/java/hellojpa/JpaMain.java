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
            Team team = new Team();
            team.setName("team");
            em.persist(team);

            Member member = new Member();
            member.setUsername(null);
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            String query = "select case when m.age <= 10 then '학생요금' when m.age <= 60 then '경로요금' else '일반요금' end from j_member m";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();

            result.forEach(System.out::println); // 학생요금

            // coalesce: 하나씩 조회해서 null 이 아니면 반환
            // nullif: 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
            String query2 = "select coalesce(m.username, '이름 없는 회원') from j_member m";
            List<String> result2 = em.createQuery(query2, String.class)
                    .getResultList();

            result2.forEach(System.out::println); // 이름 없는 회원

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
