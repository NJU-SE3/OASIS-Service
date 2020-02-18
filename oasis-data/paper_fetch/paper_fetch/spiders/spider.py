# -*- coding: utf-8 -*-
import scrapy
from bs4 import BeautifulSoup


class SpiderSpider(scrapy.Spider):
    name = 'spider'

    def start_requests(self):
        dataListUrl = "http://ieeexplore.ieee.org/rest/search"
        articleDetailUrl = "http://ieeexplore.ieee.org/xpl/articleDetails.jsp?arnumber="
        articleAbstractAuthorsUrl = "http://ieeexplore.ieee.org/xpl/abstractAuthors.jsp?arnumber="
        articleKeywordsUrl = "http://ieeexplore.ieee.org/xpl/abstractKeywords.jsp?arnumber="
        articleReferencesUrl = "http://ieeexplore.ieee.org/xpl/abstractReferences.jsp?arnumber="
        i = 6693064
        yield scrapy.Request(url=articleDetailUrl + str(i),
                             callback=self.parse)  # 爬取到的页面如何处理？提交给parse方法处理

    def parse(self, response):
        doc = response.body
        soup = BeautifulSoup(
            doc
        )
        self.log(soup.find_all('strong', recursive=True))
        self.log(soup.title)
