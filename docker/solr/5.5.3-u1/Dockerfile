ARG docker_registry=
FROM $docker_registry/openjdk-jre:8

ARG solr_version="5.5.3"

ENV VOLUME_SOLR_DATA="/var/lib/solr"		\
	VOLUME_SOLR_LOGS="/opt/solr/server/logs"	\
	SERVICE_DIR_SOLR_HOME="/opt/solr"		\
	LOG_LEVEL="WARN"

COPY rootfs/ /

RUN \
	apk --update add bash && \
	chmod +x /usr/sbin/healthcheck.sh && \
	curl --fail -sL "http://archive.apache.org/dist/lucene/solr/$solr_version/solr-$solr_version.tgz" | tar -xz -C /opt && \
	mv /opt/solr-$solr_version $SERVICE_DIR_SOLR_HOME && \
	mkdir -p $VOLUME_SOLR_DATA $VOLUME_SOLR_LOGS && \
	chown $DOCKER_USER:$DOCKER_USER $VOLUME_SOLR_DATA $VOLUME_SOLR_LOGS $SERVICE_DIR_SOLR_HOME && \
	chmod +x /docker-init.d/* && \
	rm -rf /opt/solr/docs /opt/solr/dist /opt/solr/licenses /opt/solr/example /opt/solr/contrib

WORKDIR /opt/solr
VOLUME ["/var/lib/solr","/opt/solr/server/logs"]

HEALTHCHECK --interval=5s --timeout=3s CMD healthcheck.sh

EXPOSE 8983
CMD ["/bin/sh", "-c", "$SERVICE_DIR_SOLR_HOME/bin/solr -f -d $SERVICE_DIR_SOLR_HOME/server -s $VOLUME_SOLR_DATA $CLI_PARAMS"]
