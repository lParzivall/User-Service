# Registaration User Service.
RESTful service 
* registration
* authentication and authorization with JWT acsess and refresh tokens.
* get information about users and their roles. 

Technologies
* Spring Boot
* Spring Security
* JWT
* PostgreSQL
* Flyway

Требования
* База данных userservice. username=postgres, password=psql, port=5432
* При первом запуске приложения, нужно сгенерировать классы для Grpc, выполнив maven package.После нужно проверить сгенерированные исходные файлы в указонной          директории. Если Idea не пометила их соответственно, нужно сделать это вручную. Mark Directory as -> Generated Sources Root(это же нужно сделать при первом запуске Grpc клиента)

![](src/main/resources/images/package.png) 

Описание
* Сервис управления пользователями работатет на 8080 порту
* GRPC сервер работает на 8081 порту
* GRPC клиент работает на 8082 порту https://github.com/lParzivall/GrpcClient
* По умолчанию созданы два пользователя: user и admin с соответствующими ролями
* Организован доступ к API в зависимоти от роли пользователя
![](src/main/resources/images/rights.png)

Пример работы приложения.

Аунтефикация
![](src/main/resources/images/login.png)

Авторизация(используется полученный access token)
![](src/main/resources/images/auth.png)

Запрос сервиса GrpcClient от пользователя с правами ROLE_ADMIN
![](src/main/resources/images/grpc_request.png)


