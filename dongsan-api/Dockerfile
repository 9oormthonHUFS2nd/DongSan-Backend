FROM openjdk:17

ARG JAR_FILE="build/libs/*.jar"
ARG PROFILE=dev

COPY ${JAR_FILE} dongsan.jar

ENV PROFILE=$PROFILE

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "-Duser.timezone=Asia/Seoul", "dongsan.jar"]
