FROM openjdk:10

ARG UID=1050
ARG GID=1050
ARG FILE=data.csv
ENV SERVICE_HOME=/usr/share/geolocation
ENV ARTIFACT=geolocation-assembly-1.0.0.jar
ENV FILE_PATH=${SERVICE_HOME}/${FILE}
ADD docker-entrypoint.sh /docker-entrypoint.sh

ADD target/scala-2.12/${ARTIFACT} ${SERVICE_HOME}/${ARTIFACT}
ADD ${FILE} ${SERVICE_HOME}/
RUN addgroup --gid ${GID} appusers
RUN adduser -q --disabled-password --ingroup appusers --uid ${UID} appuser
RUN echo "appuser:appuser1234!" | chpasswd

RUN chown -R appuser:appusers ${SERVICE_HOME}
RUN chmod -R o-rwx ${SERVICE_HOME}
RUN chmod 644 /etc/shadow
RUN chown -R appuser:appusers /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8083
USER appuser

ENTRYPOINT [ "/docker-entrypoint.sh" ]