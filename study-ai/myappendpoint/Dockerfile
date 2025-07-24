# 使用官方 OpenJDK 21 作为基础镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制 Maven 构建的 jar 文件到容器中
COPY target/*.jar app.jar

# 暴露应用运行的端口
EXPOSE 8080

# 设置容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]