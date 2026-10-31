# MathQuest

<h3>Языки</h3>
<p align="left">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Scala-DC322F?style=flat-square&logo=scala&logoColor=white" />
  <img src="https://img.shields.io/badge/SQL-336791?style=flat-square&logo=postgresql&logoColor=white" />
</p>

<h3>Бэкенд</h3>
<p align="left">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat-square&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white" />
  <img src="https://img.shields.io/badge/Keycloak-4D4D4D?style=flat-square&logo=keycloak&logoColor=white" />
  <img src="https://img.shields.io/badge/Apache_Kafka-231F20?style=flat-square&logo=apachekafka&logoColor=white" />
  <img src="https://img.shields.io/badge/Nginx-009639?style=flat-square&logo=nginx&logoColor=white" />
</p>

<h3>Базы данных</h3>
<p align="left">
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white" />
</p>

<h3>Инструменты</h3>
<p align="left">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white" />
  <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white" />
  <img src="https://img.shields.io/badge/Bash-4EAA25?style=flat-square&logo=gnubash&logoColor=white" />
  <img src="https://img.shields.io/badge/JUnit_5-25A162?style=flat-square&logo=junit5&logoColor=white" />
  <img src="https://img.shields.io/badge/Linux-FCC624?style=flat-square&logo=linux&logoColor=black" />
</p>



Игра по математике для 1–11 классов.

Сейчас в проекте готова лабораторная работа №1 (вариант 503302):
координатная плоскость, проверка попадания точки в область и история проверок.

## Возможности

- Проверка точки (X, Y) при заданном R с валидацией полей
- Координатная плоскость на canvas: масштаб колесиком, перемещение мышью
- История проверок с пагинацией, клик по строке показывает точку
- Светлая и тёмная тема, 12 цветовых палитр

## Область варианта

| Четверть | Фигура | Условие |
|---|---|---|
| I | прямоугольник | `x ≤ R/2`, `y ≤ R` |
| IV | треугольник | `y ≥ x − R/2` |
| III | четверть круга | `x² + y² ≤ (R/2)²` |

## Стек

HTML, CSS (переменные, без препроцессоров), JavaScript (ES-модули), Canvas API,
nginx, Docker.

## Запуск

Нужен Docker.

```
docker build -t mathquest-front .
docker run -d --rm -p 8080:80 --name mathquest-front mathquest-front
```

## Документация

- [CHANGELOG.md](docs/CHANGELOG.md) — история изменений
- [TODO.md](docs/TODO.md) — планы
- [KNOWN_ISSUES.md](docs/KNOWN_ISSUES.md) — известные проблемы

## Автор

Лишик Александра Юрьевна, группа P3206