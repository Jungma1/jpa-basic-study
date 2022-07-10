package hellojpa.jpql;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "j_order")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private int orderAmount;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
