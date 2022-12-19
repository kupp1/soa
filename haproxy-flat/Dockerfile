FROM haproxy:2.4.20
USER root

COPY server.pem /etc/haproxy/certs.d/

RUN apt-get update
RUN apt-get --assume-yes install wget procps

RUN wget https://github.com/haproxytech/dataplaneapi/releases/download/v2.4.2/dataplaneapi_2.4.2_Linux_arm64.tar.gz
RUN tar -zxvf dataplaneapi_2.4.2_Linux_arm64.tar.gz
RUN cp build/dataplaneapi /usr/local/bin/
RUN chmod +x /usr/local/bin/dataplaneapi

COPY haproxy.cfg /usr/local/etc/haproxy/haproxy.cfg
COPY dataplaneapi.yaml /etc/haproxy/dataplaneapi.yaml