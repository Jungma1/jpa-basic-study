package hellojpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DiscriminatorValue("book")
@Entity(name = "d_book")
public class Book extends Item {

    private String author;

    private String isbn;
}
