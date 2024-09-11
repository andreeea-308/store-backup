package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn
    private User user;

    private CartStatus status;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    private LocalDate checkoutDate;

    public void addCartItem(CartItem cartItem){
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
    }
    public void removeCartItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
        cartItem.setCart(null);
    }

    public float getTotal(){
        return (float) this.cartItems.stream()
                .mapToDouble(CartItem::getTotal)
                .sum();

    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
