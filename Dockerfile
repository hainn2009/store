# =========================
# Build stage
# =========================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy toàn bộ source code + pom.xml vào image
COPY . .

# Build project, skip test để nhanh hơn
RUN mvn clean package -DskipTests

# =========================
# Run stage
# =========================
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy file .jar từ stage build vào image run
COPY --from=build /app/target/*.jar app.jar

# Mở cổng 8080 (hoặc port bạn dùng)
EXPOSE 8080

# Lệnh chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]
