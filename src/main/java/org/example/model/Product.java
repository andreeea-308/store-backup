package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private float price;
    private int stock;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = CartItem.class)
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }

    public void addCartItem(CartItem cartItem){
        this.cartItems.add(cartItem);
        cartItem.setProduct(this);
    }
    public void removeCartItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
        cartItem.setProduct(null);
    }

}
