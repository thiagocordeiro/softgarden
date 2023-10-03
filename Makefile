up:
	./gradlew build
	docker-compose up -d --build
	make logs
logs:
	docker-compose logs -f

down:
	docker-compose down

test:
	./gradlew test

migrate:
	cat .migrations/create_scheduler_api_tables.sql | docker compose exec -T mysql /usr/bin/mysql -uroot -proot default
