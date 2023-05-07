package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cart")
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long code;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private  Member member;

    }

