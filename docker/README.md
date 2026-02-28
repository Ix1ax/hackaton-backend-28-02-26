# Docker (Postgres + Flyway)

## Быстрый старт (локально)

1) Создай `.env` рядом с `compose.yaml`:

```bash
cp .env.example .env
```

2) Подними Postgres и прогони миграции:

```bash
docker compose up -d postgres
docker compose run --rm flyway
```

Миграции Flyway должны лежать в `src/main/resources/db/migration` и называться как `V1__init.sql`, `V2__add_users.sql`, ...

## Запуск приложения (опционально)

Сервис `app` находится в профиле `app`:

```bash
docker compose --profile app up -d --build
```

Внутри compose для `app` выставлены переменные `SPRING_DATASOURCE_*` и отключен Spring Flyway (`SPRING_FLYWAY_ENABLED=false`), чтобы миграции выполнялись отдельным контейнером `flyway`.

