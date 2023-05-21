package com.fridgeInMyHand.fridgeInMyHandBackend.food.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

public class Food{

    @AllArgsConstructor
    public class FoodInfo {
        public String userUUID;

        public String FoodName;
        public Integer quantity;
        public LocalDateTime bestBefore;

        public Boolean isPublic;

        public String getFoodName() {
            return FoodName;
        }
    }
    @Getter
    @Setter
    public static class GetFoodInfoRequest{
        public String requestUUID;
        public String userUUID;

    }
    @Getter
    @AllArgsConstructor
    public static class GetFoodInfoResponse{
        public String foodName;
        public LocalDateTime bestBefore;
    }

    @Getter
    @Setter
    public static class FoodPostRequest{
        public String userUUID;
        public String foodName;
        public Integer quantity;
        public LocalDateTime bestBefore;

        public Boolean isPublic;

    }

}

