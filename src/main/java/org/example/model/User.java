package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    private RoleType roleType;
    private Language language;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Cart> carts;

    public void addCart(Cart cart){
        this.carts.add(cart);
        cart.setUser(this);
    }
    public void removeCart(Cart cart){
        this.carts.remove(cart);
        cart.setUser(null);
    }
}
