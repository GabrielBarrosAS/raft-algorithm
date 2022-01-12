FROM openjdk:11
WORKDIR /opt/app
RUN git clone https://github.com/GabrielBarrosAS/raft-algorithm.git /opt/app
ENTRYPOINT ["java","-jar","raft-0.0.1-SNAPSHOT.jar"]