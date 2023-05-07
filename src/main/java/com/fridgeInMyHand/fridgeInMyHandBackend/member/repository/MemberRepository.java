package com.fridgeInMyHand.fridgeInMyHandBackend.member.repository;

import com.fridgeInMyHand.fridgeInMyHandBackend.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

}
