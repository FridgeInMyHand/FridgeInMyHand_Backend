package com.fridgeInMyHand.fridgeInMyHandBackend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AnalysisController {

    @PostMapping("/analysis")
    public void doAnalysis() {
        //TODO
    }
}
