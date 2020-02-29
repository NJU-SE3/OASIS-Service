FROM mongo:latest
COPY paper.json /paper.json
ENV MONGO_INITDB_ROOT_USERNAME=root
ENV MONGO_INITDB_ROOT_PASSWORD=mongo
ENV MONGO_INITDB_ROOT_DATABASE=se
USER root
CMD mongoimport -d se -u root \
	-p mongo --collection papers --drop --file /paper.json --jsonArray

EXPOSE 27017