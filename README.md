# Spring Cloud Bank App

### Микросервисное приложение «Банк» на Spring Boot / Spring Cloud.

#### В рамках курса Яндекс.Практикум - Спринт 9

## Сервисы и технологии

- `frontend-service` — web UI (Thymeleaf), вход пользователя по OAuth2 Authorization.
- `gateway-api` — единая точка входа, маршрутизация запросов к внутренним сервисам.
- `accounts-service` — данные пользователя и счета, операции обновления профиля/баланса.
- `cash-service` — пополнение/снятие, межсервисный вызов `accounts-service`.
- `transfer-service` — перевод между счетами, межсервисный вызов `accounts-service`.
- `notifications-service` — прием сообщений об операциях (логирование сообщений).
- `config-server` — централизованный конфиг из `config-repo`.
- `zookeeper` — service discovery server.
- `keycloak` — OAuth2/OIDC server.
- `postgres` — БД для `accounts-service`.

## Требования

Java 21+ и Maven для локального запуска без Docker

## Переменные окружения (`.env`)

```env
CASH_SERVICE_CLIENT_SECRET= credentials для cash-service
TRANSFER_SERVICE_CLIENT_SECRET= credentials для transfer-service
KEYCLOAK_CLIENT_SECRET= credentials для oauth2 авторизации на frontend-service
GATEWAY_SERVICE_CLIENT_SECRET= credentials для clients-service
```

## Порядок запуска (Docker)

1. Заполнить `.env`
2. docker compose up --build
3. Проверить:
    - Keycloak: [http://localhost:9990](http://localhost:9990)
    - Config Server: [http://localhost:8888](http://localhost:8888)
    - Frontend: [http://localhost:8080](http://localhost:8080)

## Порядок запуска (локально, без Docker)

1. Настроить инфраструктуру (`zookeeper`, `keycloak`, `postgres`) через docker compose или отдельно.
2. Запустить `config-server`.
3. Запустить по очереди:
    - `accounts-service`
    - `notifications-service`
    - `cash-service`
    - `transfer-service`
    - `gateway-api`
    - `frontend-service`

## Архитектура запросов

- Пользовательский трафик: `frontend-service` -> `gateway-api` -> target service.
- Межсервисный трафик:
    - `cash-service` -> `accounts-service`
    - `transfer-service` -> `accounts-service`
    - `cash-service` / `transfer-service` -> `notifications-service`
- Для межсервисных вызовов используется OAuth2 Client Credentials.

## Endpoint-ы

### Frontend (`frontend-service`, порт `8080`)

- `GET /` -> редирект на `/account`
- `GET /login` — страница логина
- `GET /account` — основная страница
- `POST /account` — обновление профиля
- `POST /cash` — внесение/снятие денежных средств
- `POST /transfer` — перевод

### Gateway (`gateway-api`, порт `8086`)

Маршруты:

- `/account-service/**` -> `accounts-service`
- `/cash-service/**` -> `cash-service`
- `/transfer-service/**` -> `transfer-service`
- `/notification-service/**` -> `notification-service` (как задано в `gateway-api/src/main/resources/application.yml`)

### Accounts (`accounts-service`, порт `8081`)

- `GET /accounts` — получить свой аккаунт + список пользователей для перевода
- `GET /accounts/{login}` — получить аккаунт по логину
- `POST /accounts` — обновить `UserData` (валидируется, включая 18+)
- `POST /accounts/cash` — cash-операция (`CashActionDto`)
- `POST /accounts/transfer` — перевод (`TransferActionDto`)

### Cash (`cash-service`, порт `8082`)

- `POST /cash` — обработка пополнения/снятия (`CashActionDto`)
    - валидируется DTO:
        - `action != null`
        - `accountNumber != null`
        - `balance > 0`

### Transfer (`transfer-service`, порт `8083`)

- `POST /transfer` — перевод (`TransferActionDto`)
    - валидируется DTO:
        - `fromAccountId != null`
        - `toAccountId != null`
        - `amount > 0`

### Notifications (`notifications-service`, порт `8084`)

- `POST /notification` — принимает строковое сообщение и пишет в лог.

## Тесты

- Запуск тестов конкретного модуля:
  ```bash
  mvn -pl accounts-service test
  ```

## Замечания

- Конфигурация сервисов идет через `config-server` и файлы в `config-server/src/main/resources/config-repo`.
- Для тестов добавлены test-bootstrap настройки, чтобы не требовался запущенный config-server.
