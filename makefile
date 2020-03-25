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
BACK_FILES = author paper authorCitation
EXTEND_DOCS = affiliation conference field
COUNTER_DOCS = counterBase

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
# data-init: mongo-import extend-import counter-import
data-init: mongo-import extend-import

mongo-import:
	$(foreach var,$(BACK_FILES),docker cp back/${var}.json oasis-mongo:/.;)
	$(foreach var,$(BACK_FILES),docker exec -it ${MONGO} mongoimport ${MONGO_FLAGS} -c ${var}s --drop ${var}.json;)

mongo-export:
	$(foreach var,$(BACK_FILES),docker exec -it ${MONGO} mongoexport ${MONGO_FLAGS} -c ${var}s -o ${var}.json;)
	$(foreach var,$(BACK_FILES),sudo docker cp ${MONGO}:/${var}.json back/${var}.json;)

extend-export:
	$(foreach var,$(EXTEND_DOCS),docker exec -it ${MONGO} mongoexport ${MONGO_FLAGS} -c ${var}s -o ${var}.json;)
	$(foreach var,$(EXTEND_DOCS),sudo docker cp ${MONGO}:/${var}.json back/${var}.json;)

extend-import:
	$(foreach var,$(EXTEND_DOCS),docker cp back/${var}.json oasis-mongo:/.;)
	$(foreach var,$(EXTEND_DOCS),docker exec -it ${MONGO} mongoimport ${MONGO_FLAGS} -c ${var}s --drop ${var}.json;)

counter-export:
	$(foreach var,$(COUNTER_DOCS),docker exec -it ${MONGO} mongoexport ${MONGO_FLAGS} -c ${var}s -o ${var}.json;)
	$(foreach var,$(COUNTER_DOCS),sudo docker cp ${MONGO}:/${var}.json back/${var}.json;)

counter-import:
	$(foreach var,$(COUNTER_DOCS),docker cp back/${var}.json oasis-mongo:/.;)
	$(foreach var,$(COUNTER_DOCS),docker exec -it ${MONGO} mongoimport ${MONGO_FLAGS} -c ${var}s --drop ${var}.json;)

deploy-app:
	${COMPOSE} -f ${APP_DOCKER_COMPOSE} pull
	${COMPOSE} -f ${APP_DOCKER_COMPOSE} up -d

reset-data:
	${COMPOSE} -f ${DATA_DOCKER_COMPOSE} down
	${COMPOSE} -f ${DATA_DOCKER_COMPOSE} up -d

