# Spring User API

REST API приложение для управления пользователями с использованием Spring Boot 3, PostgreSQL и Docker.

## Технологический стек

- **Java 17**
- **Spring Boot 3**
- **PostgreSQL 15**
- **Liquibase** (миграции базы данных)
- **Docker & Docker Compose**
- **Maven**

## Функциональность

-  Создание нового пользователя
-  Получение информации о пользователе по UUID
-  Обновление данных пользователя
-  Удаление пользователя по UUID
-  Валидация входных данных
-  Обработка ошибок через `@ControllerAdvice`
-  Автоматическое создание схемы БД через Liquibase

## Структура базы данных

### Таблица `users`
| Столбец       | Тип    | Описание                |
|---------------|--------|-------------------------|
| uuid          | UUID   | Первичный ключ          |
| fio           | TEXT   | ФИО (обязательное)      |
| phone_number  | VARCHAR(50) | Телефонный номер     |
| avatar        | TEXT   | URL аватарки            |
| role_uuid     | UUID   | Внешний ключ к roles    |

### Таблица `roles`
| Столбец       | Тип    | Описание                |
|---------------|--------|-------------------------|
| uuid          | UUID   | Первичный ключ          |
| role_name     | VARCHAR(255) | Название роли (уникальное) |

## Быстрый старт

### Предварительные требования
- Docker
- Docker Compose
- Java 17 (для локальной разработки)

### Запуск через Docker Compose

1. **Клонируйте репозиторий:**
```bash
git clone https://github.com/muromtsev/spring-user-app.git
cd spring-user-app
```

2. **Запустите приложение:**
```bash
docker-compose up --build
```

3. **Приложение будет доступно по адресу:**
```bash
http://localhost:8080
```

## API Endpoints

### **1. Создание пользователя**

**Метод:** `POST`  
**Эндпоинт:** `/api/createNewUser`

**Тело запроса:**
```json
{
  "fio": "Иванов Иван Иванович",
  "phoneNumber": "+79161234567",
  "avatar": "https://example.com/avatar.jpg",
  "roleName": "USER"
}
```

### **2. Получение пользователя**

**Метод:** `GET`  
**Эндпоинт:** `/api/users?userUuid={uuid}`

**Ответ:**
```json
{
  "uuid": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "fio": "Иванов Иван Иванович",
  "phoneNumber": "+79161234567",
  "avatar": "https://example.com/avatar.jpg",
  "role": {
    "uuid": "b2c3d4e5-f6g7-8901-bcde-f23456789012",
    "roleName": "USER"
  }
}
```

### **3. Обновление пользователя**

**Метод:** `PUT`  
**Эндпоинт:** `/api/userDetailsUpdate`

**Тело запроса:**
```json
{
  "userUuid": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "fio": "Иванов Иван Updated",
  "phoneNumber": "+79169876543",
  "avatar": "https://example.com/new-avatar.jpg",
  "roleName": "ADMIN"
}
```

### **4. Удаление пользователя**

**Метод:** `DELETE`  
**Эндпоинт:** `/api/users?userUuid={uuid}`

**Ответ:** `204 No Content`


 
