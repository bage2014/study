# 基础镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制构建产物
COPY target/*.jar app.jar

# 复制配置文件
COPY src/main/resources/application-*.properties /app/
COPY src/main/resources/db /app/db

# 暴露端口
EXPOSE 8080

# 环境变量
ENV SPRING_PROFILES_ACTIVE=prod
ENV JWT_SECRET=familytreeappsecretkey

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]