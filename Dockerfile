# base 이미지 가져오기
# 최신 버전의 alpine 사용
# 빌드 스테이지를 builder라고 명명
FROM amazoncorretto:17-alpine-jdk AS builder

# 작업 디렉토리 /app 으로 설정
WORKDIR /app

# gradlew, build.gradle, settings.gradle, gradle, src 복사
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

# jar 파일 생성을 위한 bootJar 실행
RUN ./gradlew bootJar

# 2번째 빌드 스테이지
# base 이미지 가져오기
FROM amazoncorretto:17-alpine-jdk

# 작업 디렉토리 설정
WORKDIR /app

# jar 파일 가져오기
# 이전 빌드 스테이지 'builder'에서 생성된 jar 파일을 복사
COPY --from=builder /app/build/libs/*.jar /app/naoman.jar

# 8080 포트를 사용함을 명시
EXPOSE 8080

# 컨테이너 실행 시 naoman.jar 파일 실행
ENTRYPOINT java -jar /app/naoman.jar