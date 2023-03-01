#FROM debian:buster-slim
FROM ubuntu:latest
MAINTAINER sudobarre
COPY target/blog /app/blog
RUN apt-get -y update
RUN apt-get -y install build-essential
EXPOSE 8098
CMD ["/app/blog"]
#FROM ubuntu:latest
#COPY target/blog /app/blog
#WORKDIR /app
#RUN apt-get -y update
#RUN apt-get -y install build-essential
#EXPOSE 8098
#CMD ["/app/blog"]




#FROM eclipse-temurin:17-alpine
#MAINTAINER sudobarre
#COPY target/blog-0.0.1-SNAPSHOT.jar blog.jar
#EXPOSE 8098
#CMD ["/app/blog"]


#RUN apk add --no-cache zlib



