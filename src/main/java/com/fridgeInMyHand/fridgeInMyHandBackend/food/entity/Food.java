package com.fridgeInMyHand.fridgeInMyHandBackend.food.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@Table
@RequiredArgsConstructor
public class Food {
    @Column(name = "userID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userUUID;
    @Column
    private String foodName;

    @Column
    private LocalDateTime bestBefore;

}
