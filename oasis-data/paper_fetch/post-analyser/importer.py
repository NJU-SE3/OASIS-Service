import json

import pandas as pd
import requests
import re


def import_papers(df):
    for i , row in df.iterrows():
        if row['title'] != '':
            try:
                authors = eval(row['authors'])
            except Exception:
                authors = []
            author_names = ';'.join([author['name'] for author in authors])
            aff_names = ';'.join([aff['affiliation'] for aff in authors])
            author_ids = []
            for author in authors:
                if 'id' in author:
                    author_ids.append(author['id'])

            ref_cnt, cit_cnt = len(eval(row['references'])), len(eval(row['citations']))
            conference_name = str(row['publication']).encode('ascii', 'ignore').decode('ascii')
            from conference_analyser import conference_filter
            conference_name = conference_filter(conference_name).strip()
            paper = dict(
                id=row['id'],
                title=str(row['title']).encode('ascii', 'ignore').decode('ascii'),
                abstract=str(row['abstract']).encode('ascii', 'ignore').decode('ascii'),
                conference=conference_name,
                terms=row['keywords'],
                keywords='',
                year=row['year'],
                references=eval(row['references']),
                citations=eval(row['citations']),
                pdfLink='https://ieeexplore.ieee.org/document/' + str(row['id']),
                authorIds=author_ids,
                authors=author_names,
                affiliations=aff_names,
                citationCount=row['citationCount'] if row['citationCount'] != 'nan' else '0',
                referenceCount=ref_cnt
            )
            requests.post('http://localhost:8081/data/initialization/field', data=json.dumps(paper),
                      headers={'Content-Type': 'application/json'})

def import_authors(df):
    for i , row in df.iterrows():
        # ,id,preferredName,firstName,lastName,affiliation,coAuthors,articleCount,trends,bioParagraphs,publicTopic,terms,photoUrl
        affiliationName = row['affiliation'].encode('ascii', 'ignore').decode('ascii')
        affiliationName = re.sub(r'[().*]', "", re.sub(r'\(.*\)', "", affiliationName.strip()).strip())
        author = dict(
            id=row['id'],
            authorName=row['preferredName'].encode('ascii', 'ignore').decode('ascii'),
            affiliationName=affiliationName,
            coAuthors=row['coAuthors'],
            articleCount=row['articleCount'],
            paperTrends=row['trends'],
            bioParagraphs=row['bioParagraphs'].encode('ascii', 'ignore').decode('ascii'),
            field=row['publicTopic'].encode('ascii', 'ignore').decode('ascii'),  # 作者研究领域 summary
            terms=row['terms'],
            photoUrl=row['photoUrl']
        )
        requests.post('http://localhost:8081/query/author', data=json.dumps(author),
                      headers={'Content-Type': 'application/json'})

if __name__ == '__main__':
    # df = pd.read_csv('/Users/mac/Documents/repos/SE3/OASIS-Service/oasis-data/paper_fetch/resources/papers/author.csv')
    # df = df.fillna('')
    # import_authors(df)
    df = pd.read_csv('/Users/mac/Documents/repos/SE3/OASIS-Service/oasis-data/paper_fetch/resources/papers/paper.csv')
    df = df.fillna('')
    import_papers(df)
