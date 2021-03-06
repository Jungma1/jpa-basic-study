package hellojpa.jpql;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NamedQuery(
        name = "j_member.findByUsername",
        query = "select m from j_member m where m.username = :username"
)
@Entity(name = "j_member")
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
