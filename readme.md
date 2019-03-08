# Tiny Drive Java Spring Starter Project

This project will help you get started with Tiny Drive and allow you to play around with it's features.

## Pre requirements

Java 10+ and Maven 3.5.2+

## Getting started steps

These are the steps needed to get this project running on your machine:

### 1. Clone this repo to your local machine using

```
$ git clone git@github.com:tinymce/tinydrive-java-spring-starter.git
```

### 2. Change the `apiKey` in `tinydrive-java-spring-starter/application.yml`

You get the api key from the `API Key Manager` account manager at http://tiny.cloud.

### 3. Create a `tinydrive-java-spring-starter/src/main/resources/private.key` file containing your generated private RSA key

You get the private RSA key from the `JWT Key Manager` account manager at http://tiny.cloud.

### 4. Go to the directory and install npm packages and then start the dev server

```
$ cd tinydrive-java-spring-starter
$ mvn spring-boot:run
```

### 5. Open the example project at http://localhost:3000
