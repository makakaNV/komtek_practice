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
2. Для аудита действий пользователя добавлен интерсептор (AuditInterceptor), перехватывающий входящие запросы. Логи запросов сохраняются в logs/audit.log.
    1. AuditInterceptor перенесен в директорию util/.
3. Добавлена выгрузка результатов лаб. исследований в PDF. Добавлен эндпоинт /{id}/pdf в TestController, метод generateTestPdf в TestServiceImpl.
4. Добавлено кэширование данных. Для управления кэшированием используется `cache/CacheService` и `config/CacheConfig` с менедежером CaffeineCacheManager. В кэшировании учавствуют запросы получения данных по id `GET http://localhost:8888/api/v1/xxxx/{id}`, запросы удаления/обновления данных, а также запросы поиска пациента по ФИО/Дате рождения

## Конфигурация и запуск 
В файле `src/main/resources/application-dev.yml` нужно указать параметры подключения БД:  

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

Также указать secret key для подписи jwt-токенов

```yaml
token:
  signing:
    key: 53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75000000
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
│── logs/        # логи интерсептора
│── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── lab/
│   │   │           ├── cache/         # Кэширование
│   │   │           │   ├── impl/      # Реализация сервиса кэширования
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
│   │   │   ├── logback-spring.xml     # Конфигурация логирования
│   │   │   ├── application.yml        # Основные настройки Spring Boot
│   │   │   ├── application-dev.yml    # Настройки подключения к БД
│   │   │   ├── config/
│   │   │   │   ├── liquibase/         # Конфигурация миграций БД (Liquibase)
│   │   │   │   │   ├── changelog/     # XML-файлы миграций
│   │   │   │   │   ├── master.xml     # Основной файл Liquibase
│   │   │   ├── docs/                  # Файлы документации API 
│   │   │   ├── fonts/                 # Шрифты для пдф 
│   ├── test/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── lab/
│   │   │           ├── service/     # Тесты сервисного слоя
│── pom.xml
```
