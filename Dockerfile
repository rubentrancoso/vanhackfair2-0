FROM java

COPY build/libs/challenge-0.0.1-SNAPSHOT.jar /

COPY config/ /config

CMD [“java”, “-jar”, “challenge-0.0.1-SNAPSHOT.jar”]
