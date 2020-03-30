from py2neo import Node, Relationship, NodeMatcher

from db_connecter import connect_neo4j, connect_mongodb


def construct_graph():
    """
    构建图数据
    :return:
    """
    host = 'localhost'
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
            paper_id = paper['_id']
            selector = NodeMatcher(graph)
            items = selector.match('paper', id=paper_id)
            # 如果这个论文paper已经存在
            if len(items) > 0:
                paper_node = list(items)[0]
            # 否则创建新节点
            else:
                paper_node = Node('paper', title=paper['title'], id=paper_id)
                graph.create(paper_node)

            for author_name in paper['authors'].split(';'):
                # author entity
                entity = coll_author.find_one({'authorName': author_name})
                if entity is None: continue
                # 查找图中是否有这个author
                selector = NodeMatcher(graph)
                items = selector.match('author', id=entity['_id'])
                author_node = Node('author', authorName=author_name, id=entity['_id']) if len(items) == 0 else \
                    list(items)[0]
                R = Relationship(author_node, 'publish', paper_node)
                graph.create(R)

    # 接下来处理论文和领域
    def paper_conference():
        for paper in coll_paper.find():
            paper_id = paper['_id']
            con_name = paper['conference']
            selector = NodeMatcher(graph)
            items = selector.match('paper', id=paper_id)
            # 如果这个论文paper已经存在
            if len(items) > 0:
                paper_node = list(items)[0]
            # 否则创建新节点
            else:
                paper_node = Node('paper', title=paper['title'], id=paper_id)
                graph.create(paper_node)
            entity = coll_conference.find_one({'conferenceName': con_name, 'year': paper['year']})
            if entity is None: continue
            en_id = str(entity['_id'])
            selector = NodeMatcher(graph)
            items = selector.match('conference', id=en_id)
            con_node = Node('conference', conferenceName=con_name, id=en_id, year=entity['year']) if len(
                items) == 0 else \
                list(items)[0]
            R = Relationship(paper_node, 'published_on', con_node)
            graph.create(R)

    def author_affiliation():
        for author in coll_author.find():
            aid, author_name, aff_name = author['_id'], author['authorName'], author['affiliationName']
            selector = NodeMatcher(graph)
            items = selector.match('author', id=aid)
            # 如果这个论文paper已经存在
            if len(items) > 0:
                author_node = list(items)[0]
            # 否则创建新节点
            else:
                author_node = Node('paper', title=author['authorName'], id=aid)
                graph.create(author_node)

            entity = coll_aff.find_one({'affiliationName': aff_name})
            if entity is None: continue
            # affiliation id
            en_id = str(entity['_id'])
            selector = NodeMatcher(graph)
            items = selector.match('affiliation', id=en_id)
            aff_node = Node('affiliation', affiliationName=aff_name, id=en_id) if len(items) == 0 else \
                list(items)[0]
            R = Relationship(author_node, 'work_in', aff_node)
            graph.create(R)

    # paper_author()
    paper_conference()
    # author_affiliation()


if __name__ == '__main__':
    construct_graph()
