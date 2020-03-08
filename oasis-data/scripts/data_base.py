import json

import pandas as pd
import requests
from pandas import DataFrame
from sqlalchemy import create_engine

"""
已有的列
ASE:
Document Title 文章名称+
Authors         作者(多个) +
affiliations    机构(和前面的一一对应) +
publication title 发表论文的会议 +
publication year  发表论文的年 +
start-page      开始页(可以根据这个知道每篇论文的页数) + 
volume          ??? +
end-page        结束页 +
abstract        论文概要 +
PDF Link        论文链接 +
keywords        关键字 +
IEEE Terms , Controlled Terms , Non-Controlled Terms 术语
Citation count  文章引用计数      (citation 和 reference都需要)
Reference Count  
publisher       发布会议
"""
'''
拆分实体
作者
- id
- name (需要从authors)


隶属机构

会议

论文

'''
engine = create_engine('mysql+pymysql://root:mysql@localhost:3306/se?charset=utf8')
cols = ['Document Title', 'Authors', 'Author Affiliations', 'Publication Title', 'Date Added To Xplore',
        'Publication Year', 'Volume', 'Issue', 'Start Page', 'End Page', 'Abstract', 'ISSN', 'ISBNs', 'DOI',
        'Funding Information', 'PDF Link', 'Author Keywords', 'IEEE Terms', 'INSPEC Controlled Terms',
        'INSPEC Non-Controlled Terms', 'Mesh_Terms', 'Article Citation Count', 'Reference Count', 'License',
        'Online Date', 'Issue Date', 'Meeting Date', 'Publisher', 'Document Identifier']


def store_csv(path: 'str', name: 'str'):
    df = pd.read_csv(path)
    df.to_sql(name, engine, if_exists='append')


"""
构建用户
"""


def create_user(df: 'DataFrame', idx: 'int'):
    tmp = [row[cols[idx]] for i, row in df.iterrows()]
    ans = []
    for item in tmp:
        if type(item) is str:
            for s in item.split(';'):
                ans.append(s)

    print(ans)
    print(len(ans))


def col_formatter(df: 'DataFrame'):
    pass


def run(path):
    df = pd.read_csv(path)

    for i, row in df.iterrows():
        for key in cols:
            if pd.isna(row[key]):
                row[key] = ""
        terms = ','.join([row['IEEE Terms'], row['INSPEC Controlled Terms'], row['INSPEC Non-Controlled Terms']])
        data = {
            "title": row["Document Title"],
            "abstract": row["Abstract"],
            "conference": row["Publisher"],
            "terms": terms if terms != ",," else "",
            "keywords": row['Author Keywords'],
            "startPage": row['Start Page'],
            "endPage": row['End Page'],
            "pdfLink": row['PDF Link'],
            "citationCount": 0 if row['Article Citation Count'] == "" else row['Article Citation Count'],
            "referenceCount": 0 if row['Reference Count'] == "" else row['Reference Count'],
            "year": int(row['Publication Year']),
            "authors": row['Authors'],
            "affiliations": row['Author Affiliations']
        }
        requests.post('http://localhost:8081/api/query/paper', data=json.dumps(data),
                      headers={'Content-Type': 'application/json'})


if __name__ == '__main__':
    run('../resources/icse.csv')
    run('../resources/ase.csv')
