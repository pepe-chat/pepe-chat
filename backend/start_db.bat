docker stop jokes_db
docker rm jokes_db
docker run --rm --name pepechat -d -p 5432:5432 -e POSTGRES_PASSWORD=feelsgoodman -e POSTGRES_DB=pepechat -e POSTGRES_USER=pepe postgres