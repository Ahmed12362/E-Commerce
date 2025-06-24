package com.ShopApp.E_Commerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER) //cacade. all!!
    @JoinColumn
    @JsonBackReference
//    @JsonIgnore // for Infinite Json Loop
    private Cart cart;

    public void setTotalPrice(){
        totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }
}
