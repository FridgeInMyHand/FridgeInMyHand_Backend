FROM gradle:8.0.2-jdk17-alpine
COPY . /project
RUN  cd /project && gradle