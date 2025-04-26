# 사용할 재료
FROM openjdk:latest

# 재료를 저장할 위치
COPY build/libs/pinpick-0.0.1-SNAPSHOT.jar /app/app.jar

# 재료를 통해 요리를 할 레시피
ENTRYPOINT ["java", "-jar", "/app/app.jar"]