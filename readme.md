# Tiny Drive Java Spring Starter Project

This project will help you get started with Tiny Drive and allow you to play around with its features.

## Prerequisites

Java 10+ and Gradle 4+

## Getting started steps

These are the steps needed to get this project running on your machine:

### 1. Clone this repo to your local machine using

```
$ git clone https://github.com/tinymce/tinydrive-java-spring-starter.git
```

### 2. Change the `apiKey` in `tinydrive-java-spring-startersrc/main/resources/application.yml`

You get the API key from the `API Key Manager` account manager at https://www.tiny.cloud.

### 3. Create a `tinydrive-java-spring-starter/src/main/resources/private.key` file containing your generated private RSA key

You get the private RSA key from the `JWT Key Manager` account manager at https://www.tiny.cloud.

### 4. Go to the directory and install NPM packages and then start the development server

```
$ cd tinydrive-java-spring-starter
$ gradle bootRun
```

### 5. Open the example project at http://localhost:3000
