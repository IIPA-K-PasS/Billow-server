package com.k_passs.backend.global.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER) // 메소드 파라미터에만 붙일 수 있음
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지되어야 ArgumentResolver에서 읽을 수 있음
@Documented
public @interface AuthUser {
}
