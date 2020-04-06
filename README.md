# OASIS-Service

## OASIS 论文知识图谱平台 【服务端业务逻辑模块】

![image.png](https://i.loli.net/2020/04/06/G1bQCMipzvXxrSq.png)

### 1. 本地运行指南

#### 1.1 准备工作

##### 1.1.1 docker配置

- `windows / macOS` 系统下安装 `docker desktop` ；`linux` 下可以直接安装 `docker`
  - 配置好`docker` 的[阿里云](https://help.aliyun.com/document_detail/60750.html)仓库镜像
- 根目录下，执行 `make local-set` 。这一步主要用于启动 `mysql` , `redis` , `rabbit`，需要保证主机的 3306  , 6379 , 15672 , 5672 均未被占用
- 执行 `make mongo-import` 进行数据初始化

##### 1.1.2 maven

- 安装maven并且配置相关的阿里云代理

#### 1.2 本地运行spring

根目录下，开启两个 *terminal*，分别输入

- `mvn install -Dmaven.test.skip=true`
-  `mvn spring-boot:run -f oasis-document`

---

### 2. Todo list

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
- [x] 图数据库 *neo4j* 整合
- [x] 中间件部分实现
  - [x] Redis支持
  - [x] RabbitMQ消息队列
- [x] 测试

----

### 3. 架构设计与技术选型

##### 前端

- Vue-cli
- echarts
- axois
- Web优化
  - 懒加载

##### 服务端

- Spring cloud微服务架构
- Dev , Test , Prod 多环境配置
- 数据源：MongoDB , Mysql , Neo4j
- 数据库连接池：druid
- 优化：
  - 中间件缓存 redis
  - Mongodb , redis 连接池并发数调优

##### 其他

- 全Docker化
- Docker-compose 容器编排

