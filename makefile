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
	${COMPOSE} -f ${LOCAL_DOCKER} up -d

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
	${COMPOSE} -f ${LOCAL_DOCKER} up -d
	mvn clean test
	${COMPOSE} -f ${LOCAL_DOCKER} down

push_hub:
	mvn clean compile jib:build -Pprod

source_swarm:
	scp -r swarm/* root@101.37.116.201:/root/swarm/.