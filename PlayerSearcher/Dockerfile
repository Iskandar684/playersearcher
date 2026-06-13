FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY /target/player-searcher-0.0.1-SNAPSHOT.jar /app/player-searcher.jar
EXPOSE 8080
ENTRYPOINT ["java", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "-jar", "player-searcher.jar"]
