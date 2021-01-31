FROM openjdk:17
MAINTAINER Daniele Ulrich <daniele.ulrich@swisscom.com>
ENTRYPOINT ["/usr/bin/java","-jar", "/usr/share/softlight-far-edge/softlight-far-edge.jar"]
ARG JAR_FILE
ADD ../target/softlight-0.0.1-SNAPSHOT.jar /usr/share/softlight-far-edge/softlight-far-edge.jar
EXPOSE 8080
