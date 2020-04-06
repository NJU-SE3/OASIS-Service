from py2neo import Graph
from py2neo.ogm import GraphObject, Property, RelatedTo, RelatedFrom


# 论文
class Paper(GraphObject):
    __primarylabel__ = 'paper'

    id = Property()
    title = Property()
    authors = RelatedFrom('Author', 'publish')
    conference = RelatedTo('Conference', 'publish_on')


# 作者
class Author(GraphObject):
    __primarylabel__ = 'author'

    id = Property()
    authorName = Property()
    papers = RelatedTo('Paper', 'publish')
    affiliation_ = RelatedTo('Affiliation', 'work_in')


# 机构
class Affiliation(GraphObject):
    __primarylabel__ = 'affiliation'

    id = property()
    affiliationName = property()
    authors = RelatedFrom('Author', 'work_in')


# 会议
class Conference(GraphObject):
    __primarylabel__ = 'conference'

    id = property()
    conferenceName = property()
    papers = RelatedFrom('Paper', 'publish_on')


# 领域
class Field(GraphObject):
    __primarylabel__ = 'field'

    id = property()
    fieldName = property()


if __name__ == '__main__':
    # https://py2neo.org/v4/ogm.html#graph-objects
    graph = Graph("http://116.62.7.102:7474")
    # matcher = NodeMatcher(graph)
    res = iter(Paper.match(graph, primary_value=69602))
    en = res.__next__()

    for a in en.authors:
        print(a)
