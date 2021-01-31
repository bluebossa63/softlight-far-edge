FROM openjdk:17
LABEL maintainer="Daniele Ulrich <daniele.ulrich@swisscom.com>"
ENTRYPOINT ["/usr/bin/java","-jar", "/usr/share/softlight-far-edge/softlight-far-edge.jar"]
ARG JAR_FILE
ADD target/softlight-0.0.1-SNAPSHOT.jar /usr/share/softlight-far-edge/softlight-far-edge.jar
CMD chown -R a+r /usr/share/softlight-far-edge
EXPOSE 8080
