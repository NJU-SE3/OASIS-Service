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

### 2. 本地运行指南

#### 2.1 准备工作

##### 2.1.1 docker配置

- `windows / macOS` 系统下安装 `docker desktop` ；`linux` 下可以直接安装 `docker`
  - 配置好`docker` 的[阿里云](https://help.aliyun.com/document_detail/60750.html)仓库镜像
- 根目录下，执行 `make local-set` 。这一步主要用于启动 `mysql` , `redis` , `rabbit`，需要保证主机的 3306  , 6379 , 15672 , 5672 均未被占用
- 执行 `make mongo-import` 进行数据初始化

##### 2.1.2 maven

- 安装maven并且配置相关的阿里云代理

#### 2.2 本地运行spring

根目录下，开启两个 *terminal*，分别输入

- `mvn install -Dmaven.test.skip=true`

-  `mvn spring-boot:run -f oasis-document`

### 3. 迭代一目标

- 数据持久化与实体建模 (CSV to SQL)
- 基本CURD、聚合查询
- 其他自由发挥的查询点

`TODO list`

- [x] 爬取脚本编写
  - [x] 建模与持久化
  - [x] 会议论文爬取：确定需要爬取的论文sources
- [x] 业务逻辑编写
- [x] Druid连接池
- [x] 微服务搭建
  - [x] 统一配置
  - [x] Swarm 主从分布式架构
- [x] 多环境配置
- [x] 中间件部分实现
  - [x] Redis支持
  - [x] RabbitMQ消息队列
- [x] 测试

