FROM eclipse-temurin:23-jdk-alpine as builder

WORKDIR /application

# JAR 복사 및 압축 해제
COPY build/libs/gss-mcp-app-*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:23-jdk-alpine

WORKDIR /application

# 각 레이어를 복사
COPY --from=builder application/dependencies/ ./dependencies/
COPY --from=builder application/spring-boot-loader/ ./spring-boot-loader/
COPY --from=builder application/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=builder application/application/ ./application/

# 엔트리포인트 설정
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
