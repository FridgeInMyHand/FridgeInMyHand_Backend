package com.fridgeInMyHand.fridgeInMyHandBackend.food.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table
public class Food {
    @Column
    public String userUUID;
    @Column
    @Id
    public String foodName;
    @Column
    public String quantity;
    @Column
    private LocalDate bestBefore;
    @Column
    public Boolean isPublic;

    public Food(){}

    public Food(String userUUID, String foodName, String quantity, LocalDate bestBefore, Boolean isPublic ){
        Food food = new Food();
        food.userUUID = userUUID;
        food.foodName = foodName;
        food.quantity = quantity;
        food.bestBefore = bestBefore;
        food.isPublic = isPublic;


    }

    public Object getFoodName() {
        return foodName;
    }

    public Object getBestBefore() {
        return bestBefore;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }
    public void setQuantity (String quantity){
        this.quantity = quantity;
    }

    public void setBestBefore(LocalDate bestBefore){
        this.bestBefore = bestBefore;
    }
    public void setIsPublic(Boolean isPublic){
        this.isPublic = isPublic;
    }

    public String getQuantity() {
        return quantity;
    }
    public boolean getIsPublic(){
        return isPublic;
    }
}
