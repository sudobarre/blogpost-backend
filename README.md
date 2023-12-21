
<h3 align="center">Blogpost Backend</h3>

<div align="center">

  [![Status](https://img.shields.io/badge/status-active-success.svg)]() 

</div>

---

<p align="center"> Backend for my interactive Blogpost
    <br> 
</p>

## 📝 Table of Contents
- [About](#about)
- [Getting Started](#getting_started)
- [Deployment](#deployment)
- [Built Using](#built_using)
- [Authors](#authors)
- [Acknowledgments](#acknowledgement)

## 🧐 About <a name = "about"></a>
The backend for my interactive Blogpost to polish my skills in FullStack development that I totally didn't copy from Reddit. The frontend is available <a href="https://github.com/sudobarre/blogpost">here</a>. To see the app live, visit <a href="https://sudobarre.github.io/blogpost/">https://sudobarre.github.io/blogpost/</a>. 

<p>This app is built using Spring Boot 3. It uses JWT stored in an HTTP-Only Cookie for better security, as well as a Refresh Token feature. The package is available on ghcr.io/sudobarre/blogpost-backend:latest and it's built with GraalVM Native Build Tools to generate a native executable. To understand more about native image generation, visit <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.developing-your-first-application">here</a>. </p>

<p>I'm more than open to receive comments and constructive criticism on anything regarding the project. Feel free to contact me :)</p>

## 🏁 Getting Started <a name = "getting_started"></a>
<p>These instructions will get you a copy of the project up and running on your local machine for development purposes. See [deployment](#deployment) for notes on how to deploy the project on a live system.</p>
You can clone this repo and run it as you would normally, or pull it from Github Container Registry with the command `docker pull ghcr.io/sudobarre/blogpost-backend:latest`.


To build the project, you can either do ```mvn clean install```, or if you want to generate a native image, ```mvn clean -Pnative native:compile```. Remember to pass along the env variables as well.
### Prerequisites
<ul>
  <li>Docker</li>
  <li>GraalVM 22.3 JDK for the Java native image generation. <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.advanced">here</a> to install it.</li>
  <li>Spring Boot 3.0.4, or later. https://start.spring.io/</a></li>
</ul>

### Installing

<a href="https://docs.docker.com/get-docker/">Install Docker</a>

## 🚀 Deployment <a name = "deployment"></a>
I deployed the Docker image on <a href='https://fly.io/'>fly.io</a>, using ``` flyctl deploy --image ghcr.io/sudobarre/blogpost-backend:latest ```. See <a href="https://fly.io/docs/languages-and-frameworks/dockerfile/">here</a> for more info about deploying apps via Dockerfile to fly.io.

## ⛏️ Built Using <a name = "built_using"></a>
- [PostgreSQL](https://www.postgresql.org/) - Database
- [Spring Boot 3](https://spring.io/) - Server Framework
- [Docker](https://www.docker.com/)

## ✍️ Author <a name = "author"></a>
- [@sudobarre](https://github.com/sudobarre)

## 🎉 Acknowledgements <a name = "acknowledgement"></a>
<ul>
  <li>Initial idea taken from <a href="https://github.com/SaiUpadhyayula">Sai Subramanyam Upadhyayula</a>'s <a href="https://github.com/SaiUpadhyayula/spring-reddit-clone">Spring Reddit Clone</a></li>
  <li>This amazing <a href="https://blog.codecentric.de/spring-boot-flyio">blog post</a> by <a href="https://github.com/jonashackt">Jonas Hecht</a> that inspired me to try out and learn Java native images with GraalVM and deploy the app to fly.io</li>
  <li>HTTP-Only JWT idea taken from <a href="https://github.com/bezkoder/spring-security-refresh-token-jwt">BezKoder</a></li>
  <li>This README template taken from <a href="https://github.com/kylelobo/The-Documentation-Compendium">The Documentation Compendium</a>.</li>
</ul>
