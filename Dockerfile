#FROM openjdk:11-jdk-slim
FROM openjdk:13-jdk-alpine

#MAINTAINER xyz
LABEL maintainer="<Hüseyin Erkan Ekici> h.erkanekici@gmail.com"

COPY target/gosterBilgini_BE.jar app.jar

EXPOSE 8080

#ENTRYPOINT ["java","-jar","app.jar" ]
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]




#For external config file:

#FROM openjdk:8-jre-alpine
#LABEL maintainer="<Hüseyin Erkan Ekici> h.erkanekici@gmail.com"
#WORKDIR /app
#COPY gosterBilgini_BE.jar app.jar
#EXPOSE 8080
#CMD ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.config.location=/config/application.yml","-jar","app.jar"]