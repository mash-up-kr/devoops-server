FROM eclipse-temurin:23-jdk-alpine as builder

WORKDIR /application

# JAR 복사 및 압축 해제
COPY build/libs/gss-api-app-*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:23-jdk-alpine

WORKDIR /application

# 각 레이어를 복사
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

# 엔트리포인트 설정
ENTRYPOINT ["sh", "-c", "exec java ${RUN_JAVA_ENV} org.springframework.boot.loader.launch.JarLauncher"]
