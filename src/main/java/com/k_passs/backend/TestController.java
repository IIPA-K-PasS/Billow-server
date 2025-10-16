package com.k_passs.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController 어노테이션을 붙여서 이 클래스가 API 컨트롤러임을 알립니다.
@RestController
public class TestController {

    /**
     * 가장 기본적인 루트 경로("/")로 GET 요청이 왔을 때,
     * "Hello World!" 문자열을 반환하는 테스트용 API입니다.
     * Ingress가 기본 경로 라우팅을 제대로 하는지 확인하기 위한 목적입니다.
     * @return "Hello World! The billow-server is running!"
     */
    @GetMapping("/")
    public String healthCheck() {
        return "Hello World! The billow-server is running!";
    }

}

