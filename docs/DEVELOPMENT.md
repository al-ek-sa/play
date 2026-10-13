# Разработка

## Запуск

```
docker build -t mathquest-front .
docker run -d --rm -p 8080:80 --name mathquest-front mathquest-front
```

http://localhost:8080

После изменений в `front/` нужна пересборка.