package com.fridgeInMyHand.fridgeInMyHandBackend.food.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridgeInMyHand.fridgeInMyHandBackend.food.dto.Food;

import com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.QFood;
import com.fridgeInMyHand.fridgeInMyHandBackend.food.repository.FoodRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FoodController {
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    public FoodController(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @GetMapping("/foods")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> findAllFoods(@RequestBody Food.GetFoodInfoRequest request) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            QFood qFood = QFood.food;
            List<Map<String, Object>> foodInfoList = new ArrayList<>();

            if (request.getUserUUID().equals(request.getRequestUUID())) {
                // 요청한 유저와 본인 UUID가 일치하는 경우, 모든 음식 정보를 가져옴
                List<com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.Food> foodList = jpaQueryFactory
                        .select(qFood)
                        .from(qFood)
                        .where(qFood.userUUID.eq(request.getUserUUID()))
                        .fetch();

                for (com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.Food food : foodList) {
                    Map<String, Object> foodInfo = new HashMap<>();
                    foodInfo.put("name", food.getFoodName());
                    foodInfo.put("bestBefore", food.getBestBefore() != null ? food.getBestBefore() : null);
                    foodInfo.put("amount", food.getAmount());
                    foodInfo.put("publicFood",food.getIsPublic());
                    foodInfoList.add(foodInfo);
                }
            } else {
                // 요청한 유저와 본인 UUID가 일치하지 않는 경우, 공개된 음식 정보만 가져옴
                List<com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.Food> publicFoodList = jpaQueryFactory
                        .select(qFood)
                        .from(qFood)
                        .where(qFood.userUUID.eq(request.getRequestUUID())
                                .and(qFood.publicFood.isTrue()))
                        .fetch();

                for (com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.Food food : publicFoodList) {
                    Map<String, Object> foodInfo = new HashMap<>();
                    foodInfo.put("bestBefore", food.getBestBefore() != null ? food.getBestBefore() : null);
                    foodInfo.put("name", food.getFoodName());
                    foodInfo.put("amount", food.getAmount());
                    foodInfoList.add(foodInfo);
                }
            }

            Map<String, List<Map<String, Object>>> response = new HashMap<>();
            response.put("names", foodInfoList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/food")
    public ResponseEntity<String> addFoods(@RequestBody Food.PostFoodRequest foodRequest) {
        try {
            List<Food.FoodInfo> names = foodRequest.getFoodinfo();
            String userUUID = foodRequest.getUserUUID();
            for (Food.FoodInfo foodInfo : names) {
                String foodName = foodInfo.getFoodName();
                // 이미 존재하는 음식인지 확인
                com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.Food existingFood = foodRepository.findByFoodName(foodName);
                if (existingFood != null) {
                    // 이미 존재하는 음식일 경우 정보 수정
                    existingFood.setAmount(foodInfo.getAmount());
                    existingFood.setPublicFood(foodInfo.getPublicFood());

                    foodRepository.save(existingFood);
                } else {
                    com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.Food food = new com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.Food();
                    food.setUserUUID(userUUID);
                    food.setFoodName(foodInfo.getFoodName());
                    food.setAmount(foodInfo.getAmount());
                    food.setBestBefore(foodInfo.getBestBefore());
                    food.setPublicFood(foodInfo.getPublicFood());
                    foodRepository.save(food);
                }
            }


            return ResponseEntity.ok().build();

        } catch (Exception e) {
            // 실패한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error 상태 코드 반환
        }
    }
}
