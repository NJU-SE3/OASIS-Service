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
	mvn clean test -Dmaven.package.skip=true

push_hub:
	mvn clean package -Pprod -Dmaven.test.skip=true

source_swarm:
	scp -r swarm/* root@wxj:/root/swarm/.