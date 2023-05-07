package com.fridgeInMyHand.fridgeInMyHandBackend.member.controller;

import com.fridgeInMyHand.fridgeInMyHandBackend.member.dto.MemberDTO;
import com.fridgeInMyHand.fridgeInMyHandBackend.member.repository.MemberRepository;
import com.fridgeInMyHand.fridgeInMyHandBackend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/member/save")
    public String saveForm(){
        return "save";
    }
    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);
        MemberRepository memberRepository;
        MemberService memberService = new MemberService(memberRepository);
        memberService.save();
        return "index";
    }
}
