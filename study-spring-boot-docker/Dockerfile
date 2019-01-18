
FROM primetoninc/jdk:1.8

# 修改 study-app.jar 为自己的jar名称
ADD study-app.jar app.jar

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]
