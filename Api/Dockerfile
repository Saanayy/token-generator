FROM ubuntu:18.04

MAINTAINER Suraj Kumar "sksuraj2136@gmail.com"

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update -y && \
    apt-get install -y python3-pip python3-dev 


COPY ./requirements.txt /api/requirements.txt

WORKDIR /api

RUN pip3 install -r requirements.txt

COPY . /api

ENTRYPOINT [ "python3" ]

CMD [ "app.py" ]