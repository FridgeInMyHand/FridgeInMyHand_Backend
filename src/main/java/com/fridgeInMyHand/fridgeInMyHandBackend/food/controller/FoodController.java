package com.fridgeInMyHand.fridgeInMyHandBackend.food.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fridgeInMyHand.fridgeInMyHandBackend.controller.UserController;
import com.fridgeInMyHand.fridgeInMyHandBackend.food.dto.Food;

import com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.QFood;
import com.fridgeInMyHand.fridgeInMyHandBackend.food.repository.FoodRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.cfg.annotations.reflection.internal.XMLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @GetMapping
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
                    foodInfoList.add(foodInfo);
                }
            } else {
                // 요청한 유저와 본인 UUID가 일치하지 않는 경우, 공개된 음식 정보만 가져옴
                List<com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.Food> publicFoodList = jpaQueryFactory
                        .select(qFood)
                        .from(qFood)
                        .where(qFood.userUUID.eq(request.getRequestUUID()))
                        .fetch();

                for (com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.Food food : publicFoodList) {
                    Map<String, Object> foodInfo = new HashMap<>();
                    foodInfo.put("name", food.getFoodName());
                    foodInfo.put("bestBefore", food.getBestBefore() != null ? food.getBestBefore() : null);
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




}
