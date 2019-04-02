FROM frolvlad/alpine-oraclejdk8
VOLUME /tmp
ADD libs/${project_name}-${app_version}.jar /app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java \$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
