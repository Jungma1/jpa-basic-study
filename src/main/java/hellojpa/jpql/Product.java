package hellojpa.jpql;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "j_product")
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int price;

    private int stockAmount;
}
