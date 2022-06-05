FROM openjdk:11
ADD . /app
WORKDIR /app
RUN ./gradlew shadowJar
ENTRYPOINT java -jar build/libs/discord-1.0-SNAPSHOT-all.jar discord