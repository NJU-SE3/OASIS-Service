CONFIG_PATH = oasis-config-server
EUREKA_PATH = oasis-eureka
PAPER_PATH = oasis-paper
ZUUL_PATH = oasis-zuul
SKIP_TEST = -Dmaven.test.skip=true
JVM_RUN = java -jar
LOCAL_DOCKER = dockerfiles/docker-compose-local.yml
RUN_PATH = dockerfiles/docker-compose-prod.yml
COMPOSE = docker-compose


local-set:
	${COMPOSE} -f ${LOCAL_DOCKER} up -d --build

local-stop:
	${COMPOSE} -f ${LOCAL_DOCKER} down

run:
	${COMPOSE} -f ${RUN_PATH} up -d

stop:
	${COMPOSE} -f ${RUN_PATH} down

log:
	${COMPOSE} -f ${RUN_PATH} logs -f

clean:
	mvn clean
	bash scripts/rmi.sh

test:
	mvn clean test -Dmaven.package.skip=true -Ptest

push_hub:
	mvn clean package -Pprod -Dmaven.test.skip=true
# mongo数据导入
mongo-import:
	docker cp back/paper.json oasis-mongo:/.
	docker cp back/author.json oasis-mongo:/.
	docker cp back/authorCitation.json oasis-mongo:/.
	docker exec -it oasis-mongo mongoimport -u root -p mongo -d se -c papers --drop paper.json
	docker exec -it oasis-mongo mongoimport -u root -p mongo -d se -c authors --drop author.json
	docker exec -it oasis-mongo mongoimport -u root -p mongo -d se -c authorCitation --drop authorCitation.json

mongo-export:
	docker exec -it oasis-mongo mongoexport -u root -p mongo -d se -c papers -o paper.json
	docker exec -it oasis-mongo mongoexport -u root -p mongo -d se -c authors -o author.json
	docker exec -it oasis-mongo mongoexport -u root -p mongo -d se -c authorCitation -o authorCitation.json
	sudo docker cp oasis-mongo:/paper.json back/paper.json
	sudo docker cp oasis-mongo:/author.json back/author.json
	sudo docker cp oasis-mongo:/authorCitation.json back/authorCitation.json

deploy-app:
	docker-compose -f docker-compose-app.yml pull
	docker-compose -f docker-compose-app.yml up -d

