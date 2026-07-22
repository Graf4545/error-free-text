# Error Free Text

REST-сервис автоматической корректировки текста (исправление опечаток) с асинхронной обработкой задач и интеграцией с [API Яндекс.Спеллера](https://yandex.ru/dev/speller/doc/ru/reference/checkTexts).

## Стек

- Java 17
- Spring Boot 3
- Gradle
- PostgreSQL
- Docker / Docker Compose

## Запуск

### Docker Compose (рекомендуется)

```bash
docker compose up --build
```

Если каталог проекта называется кириллицей и Docker пишет `project name must not be empty`, в `docker-compose.yml` уже задано имя `error-free-text`. Альтернатива: `docker compose -p error-free-text up --build`.

Приложение: `http://localhost:8080`

### Локально

1. Поднять PostgreSQL (порт `5432`, БД `error_free_text`, пользователь/пароль `postgres`).
2. Сборка и запуск:

```bash
./gradlew bootRun
```

Переменные окружения (опционально): `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `SCHEDULER_FIXED_DELAY`, `SCHEDULER_BATCH_SIZE`.

## API

### Создать задачу

`POST /tasks`

```json
{
  "text": "Привет, это тестовый текст с ашибкой",
  "language": "RU"
}
```

Ответ `201 Created`:

```json
{
  "taskId": "44bd78dc-d08c-41c6-b87d-fb82046bd470"
}
```

### Получить результат

`GET /tasks/{taskId}`

- `NEW` / `IN_PROGRESS` — только статус
- `COMPLETED` — статус и `correctedText`
- `FAILED` — статус и `errorMessage`

### Ошибки

```json
{
  "errorMessage": "Task with id: 44bd78dc-d08c-41c6-b87d-fb82046bd470 not found",
  "errorCode": 40401,
  "timestamp": "2005-08-09T18:31:42.201Z",
  "path": "/tasks"
}
```

### Валидация

- `language`: `EN` или `RU`
- `text`: минимум 3 символа, не только цифры и спецсимволы

## Обработка

Планировщик периодически забирает задачи со статусом `NEW`, вызывает Yandex Speller (`POST checkTexts`), сохраняет исправленный текст. Тексты длиннее 10 000 символов режутся на части. Опции `IGNORE_DIGITS` / `IGNORE_URLS` включаются по содержимому текста.

## Тесты

```bash
./gradlew test
```

## Публикация на GitHub

```bash
git init
git add .
git commit -m "Initial commit: Error Free Text service"
git branch -M main
git remote add origin https://github.com/<username>/error-free-text.git
git push -u origin main
```

Создайте пустой репозиторий на GitHub и подставьте свой URL в `git remote add origin`.
