package com.fridgeInMyHand.fridgeInMyHandBackend.food.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public String amount;
    @Column
    private Long bestBefore;
    @Column
    public Boolean publicFood;

    public Food(){}

    public Object getFoodName() {
        return foodName;
    }

    public Long getBestBefore() {
        return bestBefore;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }
    public void setAmount (String amount){
        this.amount = amount;
    }

    public void setBestBefore(Long bestBefore){
        this.bestBefore = bestBefore;
    }
    public void setPublicFood(Boolean publicFood){
        this.publicFood = publicFood;
    }

    public String getAmount() {
        return amount;
    }
    public Boolean getIsPublic(){
        return publicFood;
    }
}
