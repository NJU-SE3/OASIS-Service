CONFIG_PATH = oasis-config-server
EUREKA_PATH = oasis-eureka
PAPER_PATH = oasis-paper
ZUUL_PATH = oasis-zuul
SKIP_TEST = -Dmaven.test.skip=true
JVM_RUN = java -jar
LOCAL_DOCKER = dockerfiles/docker-compose-local.yml
RUN_PATH = dockerfiles/docker-compose-prod.yml

local-set:
	docker-compose -f ${LOCAL_DOCKER} up -d

run:
	docker-compose -f ${RUN_PATH} up -d

clean:
	mvn clean
	bash scripts/rmi.sh

test:
	docker-compose -f ${LOCAL_DOCKER} up -d
	mvn test
	docker-compse -f ${LOCAL_DOCKER} down

push_hub:
	mvn compile jib:build -Pprod