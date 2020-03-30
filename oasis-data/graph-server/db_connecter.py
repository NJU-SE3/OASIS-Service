import pymongo
from py2neo import Graph


def connect_mongodb(host, port):
    mongo_client = pymongo.MongoClient(host, port)
    se_db = mongo_client['se']
    se_db.authenticate("root", "mongo")
    return se_db


def connect_neo4j(host, port):
    """
    https://blog.csdn.net/sinat_26917383/article/details/79901207
    :param host:
    :param port:
    :return:
    """
    graph = Graph("http://{}:{}".format(str(host), str(port)))
    return graph
