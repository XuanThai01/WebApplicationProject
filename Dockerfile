# 1. Chọn image gốc có Java 21
FROM eclipse-temurin:17-jdk

# 2. Set thư mục làm việc trong container
WORKDIR /app

# 3. Copy toàn bộ code từ root directory vào container
COPY . .

# 4. Build project bằng Maven wrapper (hoặc Maven nếu có)
RUN mvn clean package -DskipTests


# 6. Mở port để Render truy cập
EXPOSE 8080

# 7. Lệnh chạy ứng dụng
CMD ["java", "-jar", "target/WebApplicationProject-1.0-SNAPSHOT.jar"]
