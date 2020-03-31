import json

import pymongo
import scrapy
from py2neo import Graph
from py2neo import Node, Relationship, NodeMatcher


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


# https://blog.csdn.net/qq_33528613/article/details/86602707


class GraphSpider(scrapy.Spider):
    name = 'graphSpider'

    def construct_graph(self):
        host = '116.62.7.102'
        graph = connect_neo4j(host, 7474)
        # graph.delete_all()
        coll_names = ['authors', 'papers', 'affiliations', 'conferences', 'counterBases', 'fields']
        db = connect_mongodb(host, 27017)
        coll_field = db[coll_names[-1]]
        coll_author = db[coll_names[0]]
        coll_paper = db[coll_names[1]]
        coll_conference = db[coll_names[3]]
        coll_aff = db[coll_names[2]]

        def paper_author():
            """
            构建 paper - author 关系
            :return:
            """
            for paper in coll_paper.find():
                paper_id = str(paper['_id'])
                selector = NodeMatcher(graph)
                items = selector.match('paper', xid=paper_id)
                # 如果这个论文paper已经存在
                if len(items) > 0:
                    paper_node = list(items)[0]
                # 否则创建新节点
                else:
                    paper_node = Node('paper', title=paper['title'], xid=paper_id)
                    graph.create(paper_node)

                for author_name in paper['authors'].split(';'):
                    # author entity
                    entity = coll_author.find_one({'authorName': author_name})
                    if entity is None: continue
                    # 查找图中是否有这个author
                    selector = NodeMatcher(graph)
                    items = selector.match('author', xid=str(entity['_id']))
                    author_node = Node('author', authorName=author_name, xid=str(entity['_id'])) if len(items) == 0 else \
                        list(items)[0]
                    R = Relationship(author_node, 'publish', paper_node)
                    graph.create(R)
                    self.log(R)

        # 接下来处理论文和领域
        def paper_conference():
            for paper in coll_paper.find():
                paper_id = paper['_id']
                con_name = paper['conference']
                selector = NodeMatcher(graph)
                items = selector.match('paper', xid=paper_id)
                # 如果这个论文paper已经存在
                if len(items) > 0:
                    paper_node = list(items)[0]
                # 否则创建新节点
                else:
                    paper_node = Node('paper', title=paper['title'], xid=paper_id)
                    graph.create(paper_node)
                entity = coll_conference.find_one({'conferenceName': con_name, 'year': paper['year']})
                if entity is None: continue
                en_id = str(entity['_id'])
                selector = NodeMatcher(graph)
                items = selector.match('conference', xid=en_id)
                con_node = Node('conference', conferenceName=con_name, xid=en_id, year=paper['year']) if len(
                    items) == 0 else \
                    list(items)[0]
                R = Relationship(paper_node, 'published_on', con_node)
                graph.create(R)

        def author_affiliation():
            for author in coll_author.find():
                aid, author_name, aff_name = author['_id'], author['authorName'], author['affiliationName']
                selector = NodeMatcher(graph)
                items = selector.match('author', xid=aid)
                # 如果这个论文paper已经存在
                if len(items) > 0:
                    author_node = list(items)[0]
                # 否则创建新节点
                else:
                    author_node = Node('author', authorName=author['authorName'], xid=aid)
                    graph.create(author_node)

                entity = coll_aff.find_one({'affiliationName': aff_name})
                if entity is None: continue
                # affiliation id
                en_id = str(entity['_id'])
                selector = NodeMatcher(graph)
                items = selector.match('affiliation', xid=en_id)
                aff_node = Node('affiliation', affiliationName=aff_name, xid=en_id) if len(items) == 0 else \
                    list(items)[0]
                R = Relationship(author_node, 'work_in', aff_node)
                graph.create(R)

        def paper_field():
            for paper in coll_paper.find():
                paper_id = paper['_id']
                terms = json.loads(str(paper['terms']).replace("\'", "\""))['IEEE Keywords']
                selector = NodeMatcher(graph)
                items = selector.match('paper', xid=paper_id)
                # 如果这个论文paper已经存在
                if len(items) > 0:
                    paper_node = list(items)[0]
                # 否则创建新节点
                else:
                    paper_node = Node('paper', title=paper['title'], xid=paper_id)
                    graph.create(paper_node)

                for field_name in terms:
                    entity = coll_field.find_one({'fieldName': field_name})
                    if entity is None: continue
                    en_id = str(entity['_id'])
                    selector = NodeMatcher(graph)
                    items = selector.match('field', xid=en_id)
                    field_node = Node('field', fieldName=field_name, xid=en_id) if len(
                        items) == 0 else \
                        list(items)[0]
                    R = Relationship(paper_node, 'describe', field_node)
                    graph.create(R)

        paper_author()
        paper_conference()
        author_affiliation()
        paper_field()

    def start_requests(self):
        self.construct_graph()

    def parse(self, response):
        pass



