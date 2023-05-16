package com.fridgeInMyHand.fridgeInMyHandBackend.repository;

import com.fridgeInMyHand.fridgeInMyHandBackend.model.FridgeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeUserRepository extends JpaRepository<FridgeUser, Long> {
}
