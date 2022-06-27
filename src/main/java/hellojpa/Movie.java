package hellojpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DiscriminatorValue("movie")
@Entity(name = "d_movie")
public class Movie extends Item {

    private String director;

    private String actor;
}
