import csv
import json
import re

import requests

# ase
# https://ieeexplore.ieee.org/search/searchresult.jsp?queryText=ASE&highlight=true&returnType=SEARCH&returnFacets=ALL&refinements=PublicationTitle:2019%2034th%20IEEE%2FACM%20International%20Conference%20on%20Automated%20Software%20Engineering%20(ASE)&refinements=PublicationTitle:2017%2032nd%20IEEE%2FACM%20International%20Conference%20on%20Automated%20Software%20Engineering%20(ASE)&refinements=PublicationTitle:2015%2030th%20IEEE%2FACM%20International%20Conference%20on%20Automated%20Software%20Engineering%20(ASE)&refinements=PublicationTitle:2016%2031st%20IEEE%2FACM%20International%20Conference%20on%20Automated%20Software%20Engineering%20(ASE)&refinements=PublicationTitle:2013%2028th%20IEEE%2FACM%20International%20Conference%20on%20Automated%20Software%20Engineering%20(ASE)

# icse
# https://ieeexplore.ieee.org/search/searchresult.jsp?queryText=ICSE&highlight=true&returnType=SEARCH&refinements=PublicationTitle:2015%20IEEE%2FACM%2037th%20IEEE%20International%20Conference%20on%20Software%20Engineering&refinements=PublicationTitle:2018%20IEEE%2FACM%2040th%20International%20Conference%20on%20Software%20Engineering%20(ICSE)&refinements=PublicationTitle:2019%20IEEE%2FACM%2041st%20International%20Conference%20on%20Software%20Engineering%20(ICSE)&refinements=PublicationTitle:2016%20IEEE%2FACM%2038th%20International%20Conference%20on%20Software%20Engineering%20(ICSE)&refinements=PublicationTitle:2017%20IEEE%2FACM%2039th%20International%20Conference%20on%20Software%20Engineering%20(ICSE)&returnFacets=ALL

USER_AGENT = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 ' \
             'Safari/537.36 '

base_path = "G:\\code\\spider\\"


def get_keywords(keywords):
    for kds in keywords:
        if kds["type"] == 'IEEE Keywords':
            return kds["kwd"]
    return []


def get_reference(url, link_num):
    headers = {"Connection": "close", "Accept": "application/json, text/plain, */*", "cache-http-response": "true",
               "User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3377.1 Safari/537.36",
               "Referer": "https://ieeexplore.ieee.org/document/" + link_num + "/references",
               "Accept-Encoding": "gzip, deflate",
               "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8"}
    res = requests.get(url, headers=headers)
    content = json.loads(res.text)
    if 'references' in content:
        reference = content["references"]
    else:
        reference = list()
    return reference


def ieee_info(url):
    headers = {'User-Agent': USER_AGENT}
    res = requests.get(url, headers=headers)

    pattern = re.compile('metadata={.*};')
    content = json.loads(pattern.search(res.text).group()[9:-1])
    if 'title' in content:
        title = content['title']
    else:
        title = ''

    if 'authors' in content:
        authors = content['authors']
    else:
        authors = list()

    if 'abstract' in content:
        abstract = content['abstract']
    else:
        abstract = ''

    if 'publicationTitle' in content:
        publication = content['publicationTitle']
    else:
        publication = ''

    if 'keywords' in content:
        keywords = get_keywords(content['keywords'])
    else:
        keywords = list()

    if 'doi' in content:
        doi = content['doi']
    else:
        doi = ''

    paper = dict(
        title=title,
        authors=authors,
        abstract=abstract,
        publication=publication,
        keywords=keywords,
        doi=doi
    )
    return paper


def ieee_parse(csv_path):
    # ase
    ase_res = open(base_path + 'ase_res.json', 'a')
    # icse
    # icse_res = open(base_path + 'icse_res.json', 'a')

    res = []

    # flag = False

    with open(csv_path) as f:
        rows = csv.reader(f)
        headers = next(rows)
        for row in rows:
            publish_title = row[3]
            pdf_link = str(row[15])
            link_num = pdf_link[pdf_link.rfind('=') + 1:]

            # if str(link_num) == '8952245':
            #     flag = True

            # if flag is False:
            #     continue

            url = "https://ieeexplore.ieee.org/document/" + link_num

            print(url)
            paper = ieee_info(url)
            ref_url = "https://ieeexplore.ieee.org/rest/document/" + link_num + "/references"
            ref = get_reference(ref_url, link_num)

            single = dict()
            single['title'] = u''.join(paper['title']).encode('utf-8').strip()
            single['authors'] = paper['authors']
            single['abstract'] = u''.join(paper['abstract']).encode('utf-8').strip()
            single['keywords'] = u''.join(paper['keywords']).encode('utf-8').strip()
            single['publication'] = u''.join(paper['publication']).encode('utf-8').strip()
            single['doi'] = u''.join(paper['doi']).encode('utf-8').strip()
            single['ref'] = ref

            res.append(json.dumps(single))

            ase_res.write(json.dumps(single) + '\n')
            ase_res.flush()
    ase_res.close()


def format_reference(ref):
    res = list()
    for s in ref:
        if len(s.strip()) == 0:
            pass
        else:
            res.append(s)
    return res


if __name__ == '__main__':
    ieee_parse(base_path + 'ase13_15_16_17_19.csv')
    # ieee_parse(base_path + 'icse15_16_17_18_19.csv')
