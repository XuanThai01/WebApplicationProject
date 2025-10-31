# Chọn image Maven + Java
FROM maven:3.9.2-eclipse-temurin-17

# Set thư mục làm việc trong container
WORKDIR /app

# Copy toàn bộ project vào container
COPY . .

# Build project bằng Maven
RUN mvn clean package -DskipTests

# Mở port 8080
EXPOSE 8080

# Lệnh chạy ứng dụng
CMD ["java", "-jar", "target/WebApplicationProject-1.0-SNAPSHOT.jar"]
