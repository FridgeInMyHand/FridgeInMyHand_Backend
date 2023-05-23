package com.fridgeInMyHand.fridgeInMyHandBackend.food.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;


import java.time.LocalDate;
import java.util.List;

public class Food{
    public Food(String userUUID, String 음식1, String s, LocalDate now, boolean b) {
    }

    public static class FoodInfo {
        public String userUUID;
        public String foodName;
        public String quantity;
        public LocalDate bestBefore;
        public Boolean isPublic;

        public String getFoodName() {
            return foodName;
        }

        public String getUserUUID(){
            return userUUID;
        }
        public String getQuantity(){
            return quantity;
        }
        public LocalDate getBestBefore(){
            return bestBefore;
        }

        public Boolean getPublic() {
            return isPublic;
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

        public void setRequestUUID(String requestUUID) {
            this.requestUUID = requestUUID;
        }

        public void setUserUUID(String userUUID) {
            this.userUUID = userUUID;
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

