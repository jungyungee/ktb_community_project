# 빌드 단계
FROM gradle:9.5.1-jdk21 AS build
WORKDIR /app

# Gradle Wrapper 실행 스크립트
COPY gradlew ./
# Wrapper 설정 및 실행 파일
COPY gradle ./gradle
# 의존성, 플러그인, 빌드 설정
COPY build.gradle ./
# 프로젝트 기본 설정(현재는 이름만 있음)
COPY settings.gradle ./

# 복사한 파일들로 의존성 받기
# 실행 권한 추가 + 의존성 실행
RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon

# 소스코드 복사
COPY src ./src

# jar 빌드
RUN ./gradlew bootJar --no-daemon


# 실행 단계
FROM amazoncorretto:21
WORKDIR /app

#빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/community-0.0.1-SNAPSHOT.jar app.jar

# 사용 포트 설정
EXPOSE 8080

# 컨테이너가 시작 시 java -jar app.jar 실행
ENTRYPOINT ["java", "-jar", "app.jar"]