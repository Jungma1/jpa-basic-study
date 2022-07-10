package hellojpa;

import hellojpa.jpql.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);
            em.persist(member);

            // TypedQuery: 반환 타입이 명확할 때 사용
            // Query: 반환 타입이 명확하지 않을 때 사용
            TypedQuery<Member> query1 = em.createQuery("select m from j_member m", Member.class);
            Query query2 = em.createQuery("select m.username, m.age from j_member m");

            // getResultList(): 결과가 하나 이상일 때 리스트 반환 (결과가 없으면 빈 리스트 반환)
            // getSingleResult(): 결과가 정확히 하나일 때 단일 객체 반환 (결과가 없으면 NoResultException, 둘 이상이면 NonUniqueResultException)
            List<Member> members = query1.getResultList();
            Member findMember = query1.getSingleResult();

            // 파라미터 바인딩 - 이름 기준
            Member findMember2 = em.createQuery("select m from j_member m where m.username = :username", Member.class)
                    .setParameter("username", "member")
                    .getSingleResult();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
