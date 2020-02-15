import pandas as pd
from sqlalchemy import create_engine

engine = create_engine('mysql+pymysql://root:mysql@localhost:3306/se?charset=utf8')


def store_csv(path: 'str', name: 'str'):
    df = pd.read_csv(path)
    df.to_sql(name, engine, if_exists='append')


