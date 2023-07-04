FROM registry.access.redhat.com/ubi9/openjdk-17 as builder
WORKDIR /opt
COPY target/creditcardservice-0.0.1-SNAPSHOT.jar /opt/app.jar
RUN java -Djarmode=layertools -jar /opt/app.jar extract


FROM registry.access.redhat.com/ubi9/openjdk-17
WORKDIR /opt
USER root

COPY --from=builder /opt/spring-boot-loader/ ./
COPY --from=builder /opt/dependencies/ ./
COPY --from=builder /opt/snapshot-dependencies/ ./
COPY --from=builder /opt/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]