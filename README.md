# OASIS-Service

服务端业务逻辑模块

### 1. 数据源分析

#### 1.1 ASE 论文数据

映射关系

| 名称            | 关系   |
| --------------- | ------ |
| 作者 - 论文     | 多对多 |
| 作者 - 隶属机构 | 多对多 |
| 会议 - 论文     | 多对多 |

##### 作者 author

| column | description |
| ------ | ----------- |
| id     | id 作者id   |
| name   | 作者名字    |

##### 隶属机构 affiliation

| column | description |
| ------ | ----------- |
| id     |             |
| name   | 机构名称    |

##### 会议 conference

| column | description |
| ------ | ----------- |
| id     |             |
| name   | 会议名称    |

##### 论文 paper

| column          | description          |
| --------------- | -------------------- |
| id              |                      |
| title           | 论文名称             |
| abstract        | 摘要                 |
| keywords        |                      |
| Terms           | 术语，暂时放在论文内 |
| citation_count  | 引用数               |
| reference_count | 提及数               |

---

中间表

##### author_conference

| column        | description |
| ------------- | ----------- |
| author_id     |             |
| conference_id |             |
| paper_id      |             |
| start_page    |             |
| end_page      |             |
| pdf_link      |             |
| year          |             |

### 2. 迭代一目标

- 数据持久化与实体建模 (CSV to SQL)
- 基本CURD、聚合查询
- 其他自由发挥的查询点

`TODO list`

- [ ] 爬取脚本编写
  - [ ] 建模与持久化
  - [ ] 会议论文爬取：确定需要爬取的论文sources
- [ ] 业务逻辑编写
- [x] Druid连接池
- [ ] 微服务搭建
  - [ ] 统一配置
  - [ ] 熔断
- [x] 多环境配置
- [ ] 中间件部分实现
  - [x] Redis支持
  - [x] RabbitMQ消息队列
  - [ ] 接口限流
- [ ] 压力测试

