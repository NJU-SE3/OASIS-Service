# -*- coding: utf-8 -*-
import csv
import json
import re

import pandas as pd
import scrapy


def write(path, df):
    try:
        base = pd.read_csv(path, index_col=0)
        base = base.append(df, ignore_index=True)
        base.to_csv(path)
    except Exception:
        df.to_csv(path)


# csv 文件读取到列表
def parse_file(source_path_list, output_path):
    with open(output_path, 'a') as out:
        ans = []
        for source_path in source_path_list:
            with open(source_path) as read:
                rows = csv.reader(read)
                for row in rows:
                    pdf_link = str(row[15])
                    link_num = pdf_link[pdf_link.rfind('=') + 1:]
                    ans.append(link_num) if link_num.isdecimal() else None
        out.write(json.dumps(ans))
        out.flush()


def csv2list():
    paper_output = '/Users/mac/Documents/repos/spiderHome/resources/papers/paper.csv'
    num_output = '/Users/mac/Documents/repos/spiderHome/resources/papers/paper_id.txt'
    # ase
    ase_resource = '/Users/mac/Documents/repos/spiderHome/ase13_15_16_17_19.csv'
    icse_reource = '/Users/mac/Documents/repos/spiderHome/icse15_16_17_18_19.csv'

    parse_file([ase_resource, icse_reource], num_output)


class PaperspiderSpider(scrapy.Spider):
    name = 'paperSpider'
    # list_path
    list_path = '/Users/mac/Documents/repos/spiderHome/resources/papers/paper_id.txt'
    # IEEE
    ieee_base = 'https://ieeexplore.ieee.org/'
    # 论文详细
    ieee_info_url = 'https://ieeexplore.ieee.org/document/{}'
    # refer列表
    refer_url = 'https://ieeexplore.ieee.org/rest/document/{}/references'
    # 作者列表
    authors_url = 'https://ieeexplore.ieee.org/rest/document/{}/authors'
    # 作者主页
    author_base = 'https://ieeexplore.ieee.org/author/{}'
    # citation
    citations_base = 'https://ieeexplore.ieee.org/rest/document/{}/citations'
    # 输出
    paper_output = '/Users/mac/Documents/repos/spiderHome/resources/papers/paper.csv'
    # citation 递归深度
    depth_limit = 6

    # 爬取流程.
    # 1. 论文主页. 基本论文信息可以获取到
    # 2. 进入到作者列表查询, 随后进行
    # 3. 作者查询之后 , 进入citation的深度遍历
    # 3.1 对于每一个citation , 重复1 2 3
    def start_requests(self):
        with open(self.list_path) as input:
            ids = json.loads(input.readline())
            for id in ids:
                yield scrapy.Request(url=self.ieee_info_url.format(id), meta={'link_num': id})

    # 解析得到基本信息
    def parse(self, response):
        def ieee_info(content):
            def get_keywords(keywords):
                ans = {}
                for kds in keywords:
                    if 'type' in kds and 'kwd' in kds:
                        ans[kds["type"]] = kds["kwd"]
                return ans

            return dict(
                id=content['articleNumber'] if 'articleNumber' in content else '',
                title=content['title'] if 'title' in content else '',
                authors=content['authors'] if 'authors' in content else '',
                abstract=content['abstract'] if 'abstract' in content else '',
                publication=content['publicationTitle'] if 'publicationTitle' in content else '',
                keywords=get_keywords(content['keywords']) if 'keywords' in content else [],
                doi=content['doi'] if 'doi' in content else '',
                year=content['publicationYear'] if 'publicationYear' in content else '',
                citationCount=content['citationCount'] if 'citationCount' in content else '0'
            )

        text = response.text
        link_num = response.meta['link_num']
        pattern = re.compile('metadata={.*};')
        content = json.loads(pattern.search(text).group()[9:-1])
        paper = ieee_info(content)
        yield scrapy.Request(url=self.authors_url.format(link_num),
                             meta={'paper': paper, 'link_num': link_num, 'depth': 0},
                             callback=self.author_list_parse)

    # 作者爬取
    def author_list_parse(self, response):
        content = json.loads(response.text)
        paper = response.meta['paper']
        link_num = paper['id']
        # 深度优先使用
        dep_link_num = response.meta['link_num']
        # 当前填充完成作者 , 接下来进行citation
        yield scrapy.Request(url=self.refer_url.format(link_num), meta={'paper': paper,
                                                                        'link_num': dep_link_num,
                                                                        'depth': response.meta['depth']},
                             callback=self.refer_parse)

    # 爬取refer
    def refer_parse(self, response):
        content = json.loads(response.text)
        references = content['references'] if 'references' in content else []
        paper = response.meta['paper']
        link_num = paper['id']
        # 深度优先使用
        dep_link_num = response.meta['link_num']
        ans = []
        for ref in references:
            links = ref['links'] if 'links' in ref else {}
            doc_link = links['documentLink'] if 'documentLink' in links else ''
            if doc_link != '':
                ans.append(doc_link.split('/')[-1])
        paper['references'] = ans
        yield scrapy.Request(url=self.citations_base.format(link_num), meta={'paper': paper,
                                                                             'link_num': dep_link_num,
                                                                             'depth': response.meta['depth']},
                             callback=self.citation_parse)

    # 爬取citation
    def citation_parse(self, response):
        content = json.loads(response.text)
        citations = content['paperCitations'] if 'paperCitations' in content else {}
        ieee_citation_list = citations['ieee'] if 'ieee' in citations else []
        cit = []
        paper = response.meta['paper']
        link_num = paper['id']
        # 深度优先使用
        dep_link_num = response.meta['link_num']
        for citation in ieee_citation_list:
            links = citation['links'] if 'links' in citation else {}
            if 'documentLink' in links:
                cit.append(links['documentLink'].split('/')[-1])
        paper['citations'] = cit
        # 此时已经组装完成基本的内容
        write(self.paper_output, pd.DataFrame([paper]))
        # 开始深度遍历
        dep = int(response.meta['depth'])
        if dep <= self.depth_limit:
            for citation_id in paper['citations']:
                yield scrapy.Request(self.ieee_info_url.format(citation_id), meta={'link_num': citation_id,
                                                                                   'depth': response.meta['depth'] + 1},
                                     callback=self.depth_parse)
            for ref_id in paper['references']:
                yield scrapy.Request(self.ieee_info_url.format(ref_id), meta={'link_num': ref_id,
                                                                              'depth': response.meta['depth'] + 1},
                                     callback=self.depth_parse)

    def depth_parse(self, response):
        def ieee_info(content):
            def get_keywords(keywords):
                ans = {}
                for kds in keywords:
                    if 'type' in kds and 'kwd' in kds:
                        ans[kds["type"]] = kds["kwd"]
                return ans

            return dict(
                id=content['articleNumber'] if 'articleNumber' in content else '',
                title=content['title'] if 'title' in content else '',
                authors=content['authors'] if 'authors' in content else '',
                abstract=content['abstract'] if 'abstract' in content else '',
                publication=content['publicationTitle'] if 'publicationTitle' in content else '',
                keywords=get_keywords(content['keywords']) if 'keywords' in content else [],
                doi=content['doi'] if 'doi' in content else '',
                year=content['publicationYear'] if 'publicationYear' in content else ''
            )

        text = response.text
        link_num = response.meta['link_num']
        pattern = re.compile('metadata={.*};')
        content = json.loads(pattern.search(text).group()[9:-1])
        paper = ieee_info(content)
        yield scrapy.Request(url=self.authors_url.format(link_num),
                             meta={'paper': paper, 'link_num': link_num, 'depth': response.meta['depth']},
                             callback=self.author_list_parse)


class AuthorSpider(scrapy.Spider):
    name = 'authorSpider'
    ieee_base = 'https://ieeexplore.ieee.org/'

    # papers
    paper_resource_path = '/Users/mac/Documents/repos/spiderHome/resources/papers/paper.csv'

    # 作者主页
    author_base = 'https://ieeexplore.ieee.org/author/{}'
    #
    author_rest_base = 'https://ieeexplore.ieee.org/rest/author/{}'
    # output
    author_output_path = '/Users/mac/Documents/repos/spiderHome/resources/papers/author.csv'

    def start_requests(self):
        df = pd.read_csv(self.paper_resource_path)
        for i, row in df.iterrows():
            if type(row['authors']) is str:
                authors = eval(row['authors'])
                if authors is None:
                    authors = []
                for author in authors:
                    if 'id' in author:
                        author_id = author['id']
                        yield scrapy.Request(url=self.author_base.format(author_id), meta={'id': author_id})

    def parse(self, response):
        id = response.meta['id']
        yield scrapy.Request(url=self.author_rest_base.format(id), callback=self.author_detail_parse)

    def author_detail_parse(self, response):
        content = json.loads(response.text)[0]
        cos = []
        if 'coAuthors' in content:
            for item in content['coAuthors']:
                if 'id' in item:
                    cos.append(item['id'])

        trends = [
            dict(
                year=Y['name'] if 'name' in Y else '',
                numRecords=Y['numRecords'] if 'numRecords' in Y else ''
            ) for Y in (content['publicationYears'] if 'publicationYears' in content else [])
        ]
        terms = [
            dict(
                term=T['name'] if 'name' in T else '',
                numRecords=T['numRecords'] if 'numRecords' in T else ''
            ) for T in (content['indexTerms'] if 'indexTerms' in content else [])
        ]

        author = dict(
            id=content['id'] if 'id' in content else '',
            preferredName=content['preferredName'] if 'preferredName' in content else '',
            firstName=content['firstName'] if 'firstName' in content else '',
            lastName=content['lastName'] if 'lastName' in content else '',
            affiliation=content['currentAffiliation'] if 'currentAffiliation' in content else '',
            coAuthors=cos,
            articleCount=content['articleCount'] if 'articleCount' in content else '',
            trends=trends,
            bioParagraphs=content['bioParagraphs'] if 'bioParagraphs' in content else '',
            publicTopic=content['indexTermsCSV'] if 'indexTermsCSV' in content else '',
            terms=terms,
            photoUrl=self.ieee_base + (content['photoUrl']) if 'photoUrl' in content else ''
        )
        data = pd.DataFrame([author])
        write(self.author_output_path, data)
