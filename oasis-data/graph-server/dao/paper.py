# dao logic for dao mapping
from py2neo import Graph, Record

graph = Graph("http://116.62.7.102:7474")


def build_nodes(record: 'Record'):
    """
    构建nodes
    :param record:
    :return:
    """
    ans = {}
    for k, v in record['n'].items():
        ans.setdefault(k, v)
    return ans


def build_edges(record: 'Record'):
    return dict(
        start_node=record['p'].start_node['id'],
        end_node=record['p'].end_node['id'],
        type=list(record['p'].types())[0]
    )


def query_post_process(node_set, edge_set):
    """
    结果json逻辑封装
    :param node_set:
    :param edge_set:
    :return:
    """
    return dict(
        nodes=list(map(build_nodes, node_set)),
        edges=list(map(build_edges, edge_set))
    )


if __name__ == '__main__':
    # relation
    pubs = graph.run(cypher='MATCH p=()-[r:publish]->() RETURN p LIMIT 25')
    # node
    authors = graph.run(cypher='MATCH (n:author) RETURN n LIMIT 25')

    print(query_post_process(authors, pubs))