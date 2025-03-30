# LabRestApi

## Описание  
**LabRestApi** – REST API сервис для лабораторной информационной системы, позволяет регистрировать пациентов, создавать заявки на лабораторные исследования и получать результаты анализов.

## Технологический стек
Java 17
Spring Boot 3
PostgreSQL
Spring Data JPA
Maven  

## Недавние изменения
1. application.properties заменен на application.yml. URL, username, password вынесены в application-dev.yml, конфигурация добавлена в .gitignore
2. К сущностям добавлена аннотация @Builder
3. Миграции разбиты на разные файлы, директория config/liquibase/changelog/, добавлен master.xml
4. Новый метод в сервисе PatientServiceImpl - searchPatients, принимает lastName, firstName, middleName, birthDate. Осуществляет поиск по ФИО и/или дате рождения
5. DTO сущности разделены на requestDTO и responseDTO
6. К сущностям добавлены аннотации @Table, @Column и @Schema
7. Убран @Autowired
8. Сделаны мапперы для каждого сервиса
9. Сделана пагинация для методов findall() в сервисах
10. Сервисы наследуются от интерфейсов

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
Сгенерированная документация доступна по ссылке: 
https://makakanv.github.io/komtek_practice/
