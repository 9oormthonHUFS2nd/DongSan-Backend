package com.dongsan.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "✅ 헬스체크", description = "Health Check")
@Slf4j
public class HealthController {

    @Operation(summary = "애플리케이션 헬스체크")
    @GetMapping("/health")
    public String health(){
        return "Server is Healthy!";
    }
}
