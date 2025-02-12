package com.dongsan.api.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "개발용 API")
@Slf4j
public class HealthController {

    @Operation(summary = "애플리케이션 헬스체크")
    @GetMapping("/health")
    public ResponseEntity<SuccessResponse<String>> health(){
        return ResponseFactory.ok("Server is Healthy!");
    }

    @Operation(summary = "홈 헬스체크")
    @GetMapping("/")
    public ResponseEntity<SuccessResponse<String>> home(){
        return ResponseFactory.ok("It's Home!");
    }
}
