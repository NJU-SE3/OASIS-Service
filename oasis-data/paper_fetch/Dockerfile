FROM python
#WORKDIR /usr/app

COPY ./requirements.txt ./
RUN pip install -i https://pypi.tuna.tsinghua.edu.cn/simple --no-cache-dir -r requirements.txt
COPY ./ ./

CMD ["scrapy","crawl","graphSpider"]