CONFIG_PATH = oasis-config-server
EUREKA_PATH = oasis-eureka
PAPER_PATH = oasis-paper
ZUUL_PATH = oasis-zuul
MONGO = oasis-mongo
SKIP_TEST = -Dmaven.test.skip=true
JVM_RUN = java -jar
LOCAL_DOCKER = dockerfiles/docker-compose-local.yml
RUN_PATH = dockerfiles/docker-compose-prod.yml
COMPOSE = docker-compose
APP_DOCKER_COMPOSE = docker-compose-app.yml
DATA_DOCKER_COMPOSE = docker-compose-data.yml
MONGO_FLAGS = -u root -p mongo -d se

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
	mvn clean package -Dmaven.test.skip=true -Pprod
# mongo数据导入
mongo-import:
	docker cp back/paper.json oasis-mongo:/.
	docker cp back/author.json oasis-mongo:/.
	docker cp back/authorCitation.json oasis-mongo:/.
	docker exec -it ${MONGO} mongoimport ${MONGO_FLAGS} -c papers --drop paper.json
	docker exec -it ${MONGO} mongoimport ${MONGO_FLAGS} -c authors --drop author.json
	docker exec -it ${MONGO} mongoimport ${MONGO_FLAGS} -c authorCitation --drop authorCitation.json

mongo-export:
	docker exec -it ${MONGO} mongoexport ${MONGO_FLAGS} -c papers -o paper.json
	docker exec -it ${MONGO} mongoexport ${MONGO_FLAGS} -c authors -o author.json
	docker exec -it ${MONGO} mongoexport ${MONGO_FLAGS} -c authorCitation -o authorCitation.json
	sudo docker cp ${MONGO}:/paper.json back/paper.json
	sudo docker cp ${MONGO}:/author.json back/author.json
	sudo docker cp ${MONGO}:/authorCitation.json back/authorCitation.json

deploy-app:
	${COMPOSE} -f ${APP_DOCKER_COMPOSE} pull
	${COMPOSE} -f ${APP_DOCKER_COMPOSE} up -d

reset-data:
	${COMPOSE} -f ${DATA_DOCKER_COMPOSE} down
	${COMPOSE} -f ${DATA_DOCKER_COMPOSE} up -d

