# --- Spring Boot Multi-stage Dockerfile 예제 (Gradle 기준) ---

# =============================================
# STAGE 1: 코드를 빌드하여 .jar 파일을 만드는 단계
# =============================================
FROM openjdk:17-jdk as builder

# 작업 공간 설정
WORKDIR /workspace/app

# 빌드에 필요한 파일들을 먼저 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 소스코드 전체를 복사
COPY src src

# Gradle을 이용해 프로젝트를 빌드 (실행 가능한 .jar 파일 생성)
# 이 명령어를 실행하면 build/libs/ 폴더 안에 .jar 파일이 생성됩니다.
RUN ./gradlew build

# =============================================
# STAGE 2: 빌드된 .jar 파일만으로 실제 실행용 이미지를 만드는 단계
# =============================================
FROM openjdk:17-jdk-slim

# 작업 공간 설정
WORKDIR /app

# 위 'builder' 스테이지에서 생성된 .jar 파일을 복사해옴
# --from=builder 옵션이 핵심입니다.
COPY --from=builder /workspace/app/build/libs/*.jar app.jar

# 스프링 부트 앱은 보통 8080 포트를 사용
EXPOSE 8080

# 컨테이너가 시작될 때 이 명령어로 .jar 파일을 실행
ENTRYPOINT ["java","-jar","app.jar"]