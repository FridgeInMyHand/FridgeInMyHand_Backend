package com.fridgeInMyHand.fridgeInMyHandBackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
