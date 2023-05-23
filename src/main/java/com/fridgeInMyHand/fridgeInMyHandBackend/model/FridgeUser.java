package com.fridgeInMyHand.fridgeInMyHandBackend.model;

import jakarta.persistence.*;

@Entity
@Table
public class FridgeUser {
    @Id
    @Column(nullable = false)
    public String userUUID = null;

    @Column(nullable = true)
    public Double latitude = null;

    @Column(nullable = true)
    public Double longitude = null;

    @Column(nullable = true)
    public String userURL = null;
}
