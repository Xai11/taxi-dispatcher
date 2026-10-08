# taxi-dispatcher

Два микросервиса: `ride-api` (REST, наружу) и `dispatch-service` (только gRPC, внутри).

```
taxi-dispatcher/
├── proto/dispatch.proto   # общий gRPC-контракт
├── ride-api/              # REST :8080, gRPC-клиент, PostgreSQL
└── dispatch-service/      # gRPC :9090, Redis GEO + PostgreSQL
```

## Запуск

```bash
docker compose up -d ride-db dispatch-db redis
mvn -pl dispatch-service spring-boot:run
mvn -pl ride-api spring-boot:run
```

Или всё целиком: `docker compose up --build`.

## Сборка

```bash
mvn clean package
```

Java-классы gRPC генерируются из `proto/dispatch.proto` в обоих модулях.
