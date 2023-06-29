# Grimpan - Emodiary Spring Web Server

Emodiary는 Spring Web Server를 주축으로 서버가 가동합니다. <br>

-   Java Version : 17 이상
-   Server : Spring
-   MYSQL Schema : grimpan
-   requirement - 아래에 해당하는 파일을 resources 폴더에 넣어주세요
    1. application-server.yml : [] 부분을 채워주세요

<br>

# Server 사용 예제

### 0 - 1. Gradle Build - windows

> Windows용 Gradle Build를 합니다.

```cmd
.\gradlew.bat build
```

### 0 - 2. Gradle Build - linux

> Linux용 Gradle Build를 합니다.

```sh
gradlew build
```

### 1. application-server.yml 작성(아래의 파일 참조)

> Host Directory와 연결하기 때문에, image path를 수정하지 않습니다.

```yml
spring:
    datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://[DB IP]:3306/grimpan?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true
        username: [username]
        password: [password]
        hikari:
            pool-name: jpa-hikari-pool
            maximum-pool-size: 5
            jdbc-url: ${spring.datasource.url}
            username: ${spring.datasource.username}
            password: ${spring.datasource.password}
            driver-class-name: ${spring.datasource.driver-class-name}
            data-source-properties:
                rewriteBatchedStatements: true
    # JPA ??
    jpa:
        generate-ddl: true
        hibernate:
            ddl-auto: update
        show-sql: true
        properties:
            hibernate:
                dialect: org.hibernate.dialect.MySQL8Dialect
                hbm2ddl.import_files_sql_extractor: org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor
                current_session_context_class: org.springframework.orm.hibernate5.SpringSessionContext
                default_batch_fetch_size: ${chunkSize:100}
                jdbc.batch_size: 20
                order_inserts: true
                order_updates: true
                format_sql: true
    servlet:
        multipart:
            max-file-size: 10MB
            max-request-size: 10MB
    image.path: "//app//images//"
    url.path: [Server URL]

server:
    port: 8080

gpt:
    url: "https://api.openai.com/v1/chat/completions"
    key: [ChatGPT 3.5 Key]

karlo:
    url: "http://172.19.0.5:8000/create_image/"
    key: [Karlo 3.5 Key]
```

### 2. Build Docker Image

> Docker Image Build를 합니다.

```sh
docker build -t {ImageName}:{Version} .
```

### 3. Push Docker Hub

> Server에서 Pull 받기 위해, Docker Image 자신의 계정으로 Push 합니다.

```sh
docker push {UserName}/{ImageName}:{Version}
```

### 4. Pull Docker Image In Hub

> Server에서 Pull 받습니다.

```sh
docker pull {UserName}/{ImageName}:{Version}
```

### 5. Run Spring Project In Server Using Docker Image

> 해당 Project에서는 Docker Network를 통해 내부 네트워크를 구성하였습니다.<br>
> 여기서는 설명하지 않으므로 자료 찾아서 구성해주시길 바랍니다.<br>
> 또한, Image Server와 통신할 때 내부망을 사용하여 접속합니다.<br>
> Nginx를 이용한 구성은 다루지 않습니다.

```sh
docker run --name {ImageName} -d -v {HostDirectoryPath}:{ImageDirectoryPath} -p 8080:8080 --network {network-name} --ip {내부 IP} {UserName}/{ImageName}:{Version}
```

# 참고자료

-   Docker Network : https://captcha.tistory.com/70
-   Spring Docker 배포 : https://ttl-blog.tistory.com/761
-   ChatGPT 적용 : https://www.youtube.com/watch?v=NW_VcO48Rtg
