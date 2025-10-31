# 1️⃣ Chọn image Maven + Java
FROM maven:3.9.2-eclipse-temurin-17

# 2️⃣ Set thư mục làm việc trong container
WORKDIR /app

# 3️⃣ Copy toàn bộ project vào container
COPY . .

# 4️⃣ Build project bằng Maven (Spring Boot Maven Plugin sẽ tạo executable JAR)
RUN mvn clean package -DskipTests

# 5️⃣ Mở port 8080
EXPOSE 10000

# 6️⃣ Lệnh chạy ứng dụng
CMD ["java", "-jar", "target/WebApplicationProject-1.0-SNAPSHOT.jar"]

RUN echo "PORT = $PORT"
