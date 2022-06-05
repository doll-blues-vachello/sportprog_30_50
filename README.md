# Спортивное программирование задания на 30 и 50 баллов
Требуется java 11+ (тестировалось на 11)
## Сборка
``./gradlew shadowJar``
## Запуск (30 баллов)
`` java -jar build/libs/discord-1.0-SNAPSHOT-all.jar cli``
## Использование (30 баллов)
Запустить программу, ввести имя пользователя, нажать энтер
Если выдаёт непонятные ошибки - проверьте, что api codeforces работает

## Запуск (50 баллов)
``java -jar build/libs/discord-1.0-SNAPSHOT-all.jar discord --token <bot token>``

## Использование (50 баллов)
- Добавить бота на сервер
- Написать в текстовый канал имена пользователей через запятую без пробелов
- Подождать, пока бот ответит

## Запущенный бот
[https://discord.com/api/oauth2/authorize?client_id=982682501585719307&permissions=3072&scope=bot](https://discord.com/api/oauth2/authorize?client_id=982682501585719307&permissions=3072&scope=bot)