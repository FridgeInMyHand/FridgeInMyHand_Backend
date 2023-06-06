package com.fridgeInMyHand.fridgeInMyHandBackend.food.dto;

import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.List;

public class Food{

    public static class FoodInfo {
        public String userUUID;
        public String foodName;
        public String amount;
        public Long bestBefore;
        public Boolean publicFood;

        public String getFoodName() {
            return foodName;
        }

        public String getAmount(){
            return amount;
        }
        public Long getBestBefore(){
            return bestBefore;
        }

        public Boolean getPublicFood() {
            return publicFood;
        }
    }

    public static class GetFoodInfoRequest{
        public String requestUUID;
        public String userUUID;

        public String getRequestUUID() {
            return requestUUID;
        }

        public String getUserUUID(){
            return userUUID;
        }

    }

    public static class GetFoodInfoResponse{
        private List<FoodInfo> names;
    }


    public static class PostFoodRequest{
        public String userUUID;
        public List<FoodInfo> names;

        public List<FoodInfo> getFoodinfo() {
            return names;
        }

        public String getUserUUID() {
            return userUUID;
        }
    }

}

