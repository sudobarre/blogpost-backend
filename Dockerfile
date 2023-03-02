FROM ubuntu:latest
MAINTAINER sudobarre
COPY target/blog /app/blog
RUN apt-get -y update
RUN apt-get -y install build-essential
EXPOSE 8098
CMD ["/app/blog"]



