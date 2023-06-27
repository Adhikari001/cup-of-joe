package com.example.cupofjoe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller", referencedColumnName = "id")
    private MyUser seller;

    @Column(name = "ordered_status")
    private String orderStatus;

    @OneToOne
    @JoinColumn(name = "buyer", referencedColumnName = "id")
    private MyUser buyer;
}