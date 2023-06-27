package com.example.cupofjoe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "oder_quantity")
    private int orderQuantity;

    @Column(name = "additional_information")
    private String additionalInformation;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Orders orders;
}
