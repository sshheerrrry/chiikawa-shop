# 第一階段：使用 Maven + Java 17 編譯 Spring Boot
FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /app

# 先複製 pom.xml，下載 Maven dependencies
COPY pom.xml .

RUN mvn dependency:go-offline -B

# 再複製程式碼
COPY src ./src

# 編譯並產生 jar
RUN mvn clean package -DskipTests


# 第二階段：使用 Java 17 執行 Spring Boot
FROM eclipse-temurin:17-jre

WORKDIR /app

# 從 build 階段取得產生的 jar
COPY --from=build /app/target/*.jar app.jar

# Spring Boot 使用 8080
EXPOSE 8080

# 啟動 Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]