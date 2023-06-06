package com.fridgeInMyHand.fridgeInMyHandBackend.food.repository;

import com.fridgeInMyHand.fridgeInMyHandBackend.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

    @Override
    void deleteAll();

}
