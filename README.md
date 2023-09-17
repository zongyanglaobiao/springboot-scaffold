#  这个项目是干什么的
- 提供基础框架，让你进行快速开发。对于每次新建项目时，各种的建包、基础的日志监控、以及提供标准的日志格式文件等等...通用的功能太耽误我们的时间于是，于是萌生这个想法将这些东西抽离出来，使用的时候将他拉取下来改文件夹名字，就搭建好了基础框架。就像是代码生成器
- 记得我学习spring-boot的时候，有一句话到现在仍记得`约定大于配置`,所以我希望使用本项目的遵从我的约定，当然你也可以进行改动
## 目前已搭建好

1. API请求日志监控
2. 全局异常日志监控
3. 提供日志文件格式
4. 全局响应类
5. Redis序列化
6. swagger通用配置
7. knife4j框架集成
8. 跨域配置
9. 通用的yml配置

## 目前已集成的依赖

### spring-boot帮我们做版本依赖

1. `spring-boot-starter-web`:servlet 
2. `spring-boot-starter-aop`:aop编程
3. `spring-boot-starter-test`:模块测试
4. `spring-boot-starter-validation`:参数校验
5. `spring-boot-starter-data-redis`:redis集成
6. `mysql-connector-j`:MySQL驱动
7. `spring-boot-starter-jdbc`:基于MySQL驱动提供jdbc简单编程
8. `lombok`:get/set方法

### 需要去官网/仓库去看版本信息

1. `hutool-all`:java语法糖
2. `knife4j-openapi3-jakarta-spring-boot-starter`:基于swagger的接口测试框架
3. `mybatis-plus-boot-starter`:用代码写SQL
4. `pagehelper-spring-boot-starter`:分页工具

### 测试说明

1. 在`com.template.controller`包下有三个测试文件测试项目集成的依赖、功能能否使用。当然这样存放文件是不标准，测试结束没问题可以删除，注意exception包的文件不可以删除可以修改
   - Redis集成测试
   - MySQL集成测试
   - 日志监控测试
2. 测试接口框架：查看`localhost:8080/doc.html`
3. 在`application-dev.yml`文件中
   - redis：需要连接自己的
   - mysql：需要来连接自己数据库，**script/init.sql**提供了sql脚本请务必使用，否则关于MySQL的测试可能失败

## 启动类说明

1. 项目中能使用并带有`Enable`的注解都已经加上




## 包介绍
- **文件存放请按照图中来**
```text
main/
├── java
│   └── com
│       └── template
│           ├── annotation        自定义注解包
│           ├── Application.java  启动类
│           ├── config         配置包
│           ├── controller     controller层
│           │   ├── exception
│           │   │   └── ExceptionController.java  全局异常类
│           │   ├── TestController.java   测试类
│           │   ├── TestUseEntity.java   测试类
│           │   └── TestUserMapper.java  测试类
│           ├── core  核心包，这里面都是增加的功能一般不用动这个包，也不要把文件创建在这里
│           │   ├── log   
│           │   │   ├── BaseLog.java
│           │   │   └── MonitorAPI.java 
│           │   ├── redis
│           │   │   └── config  
│           │   │       └── RedisConfig.java
│           │   ├── resp
│           │   │   ├── Resp.java  全局响应类
│           │   │   └── R.java  自定义响应需要实现这个接口
│           │   ├── swagger
│           │   │   └── SwaggerConfig.java
│           │   └── web
│           │       └── SpringMvcWebConfig.java  跨域配置
│           ├── dictionary  字典包
│           ├── domain   领域包，可以自定义以下包是推荐的
│           │   ├── dto  
│           │   ├── service
│           │   └── vo
│           ├── entities  ActiveRecord包，也就是实体类包
│           ├── exception 自定义异常包
│           ├── filter 过滤器包
│           ├── interceptor 拦截器包
│           ├── mapper 相当于mapper包但是这样命名更加规范
│           └── utils  工具包
└── resources
    ├── application-config.yml
    ├── application-dev.yml
    ├── application.yml    
    ├── logback.xml  日志格式文件
    ├── mapper  mybatis文件
    └── script
        └── init.sql  sql脚本
```