#
# Build stage
#
FROM maven:3.6.0-jdk-8-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn install:install-file -Dfile=/home/app/src/main/resources/Fitness.jar -DgroupId=com.hsh -DartifactId=fitness -Dversion=1.0 -Dpackaging=jar
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:8-jre-slim
COPY --from=build /home/app/target/MasterProject2023-1.0-SNAPSHOT.jar /usr/local/lib/MasterProject2023-1.0-SNAPSHOT.jar
COPY --from=build /home/app/src/main/resources/tsp /tsp
COPY --from=build /home/app/src/main/resources/sop /sop

ENV FIRST_ARGUMENT=""
ENV TOTAL_ITERATIONS=""
ENV WHALE_POPULATION=""
ENTRYPOINT java -jar /usr/local/lib/MasterProject2023-1.0-SNAPSHOT.jar ${FIRST_ARGUMENT}
