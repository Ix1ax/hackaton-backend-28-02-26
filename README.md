## Hackaton backend

Spring Boot (Java 17) + PostgreSQL + Flyway + JWT.

### Быстрый старт (Docker Compose)

1) Подготовь `.env`:

```bash
cp env.example .env
```

Открыть `.env` и заполнить:


2) Поднять все:

```bash
docker compose up -d --build
```

3) Проверь, что бек жив:

```bash
curl http://localhost:8080/api/health
```

Swagger / OpenAPI (тестирование + документация API):
- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/v3/api-docs`

Остановить:

```bash
docker compose down
```

