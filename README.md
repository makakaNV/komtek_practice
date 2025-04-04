# LabRestApi

## Описание  
**LabRestApi** – REST API сервис для лабораторной информационной системы, позволяет регистрировать пациентов, создавать заявки на лабораторные исследования и получать результаты анализов.

## Технологический стек
* Java 17
* Spring Boot 3
* PostgreSQL
* Spring Data JPA
* Maven  

## Недавние изменения
1. Добавлена аутентификация/авторизация на основе JWT

## Конфигурация и запуск 
В файле `src/main/resources/application-dev.yml` нужно указать параметры подключения:  

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:xxxx/yourdb
    username: username
    password: password
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
    show-sql: true
```

Собрать и запустить проект можно через Maven:
```
mvn clean install
java -jar target/LabRestApi.jar
```

## Документация

### Swagger UI
Локальная API-документация доступна по ссылке:
http://localhost:8080/swagger-ui/index.html#/

### GitHub Pages
~~Сгенерированная документация доступна по ссылке: 
https://makakanv.github.io/komtek_practice/~~

## Структура проекта

```
LabRestApi/
│── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── lab/
│   │   │           ├── config/        # Конфигурация
│   │   │           ├── controller/    # REST-контроллеры
│   │   │           ├── dto/           # DTO объекты
│   │   │           │   ├── request/   # DTO для входящих запросов
│   │   │           │   ├── response/  # DTO для ответов API
│   │   │           ├── entity/        # Entity объекты
│   │   │           ├── exception/     # Исключения
│   │   │           ├── mapper/        # Мапперы для преобразования сущностей
│   │   │           ├── repository/    # Репозитории
│   │   │           ├── security/      # Безопасность
│   │   │           ├── service/       # Сервисы
│   │   │           │   ├── impl/      # Реализация сервисов
│   │   │           ├── validator/     # Кастомные аннотации и валидаторы
│   │   │           ├── util/          # Утилиты
│   │   │           │   ├── impl/      # Реализация валидаторов
│   │   ├── resources/
│   │   │   ├── application.yml      # Основные настройки Spring Boot
│   │   │   ├── application-dev.yml  # Настройки подключения к БД
│   │   │   ├── config/
│   │   │   │   ├── liquibase/       # Конфигурация миграций БД (Liquibase)
│   │   │   │   │   ├── changelog/   # XML-файлы миграций
│   │   │   │   │   ├── master.xml   # Основной файл Liquibase
│   │   │   ├── docs/                # Файлы документации API 
│   ├── test/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── lab/
│   │   │           ├── service/     # Тесты сервисного слоя
│── pom.xml
```
